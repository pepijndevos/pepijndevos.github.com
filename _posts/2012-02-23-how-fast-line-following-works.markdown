---
author: pepijndevos
comments: true
date: 2012-02-23 22:29:16+00:00
excerpt: None
layout: post
link: http://studl.es/2012/02/how-fast-line-following-works/
slug: how-fast-line-following-works
title: How fast line following works
wordpress_id: 151
categories:
- studl.es
- Code
- Tutorial
---

When using Robotic Invention System, or NXT-G for programming a robot, line following is usually done like this:

If the light is more than 50, turn left, else turn right.

This results in a slow scanning motion. It works fine for a first time, but soon, you'll want to go faster.

I used to think that you just needed 2 light sensors, one on both sides of the line, so that you could go straight if both where white, and turn towards the one that becomes back. There is a better way.

When the light sensor is on the edge of the line, does it see black or white? In fact it sees a bit of both, so you get something in between. The trick is to think of the line as a gradient, like so.

<a href="http://studl.es/wp-content/uploads/2012/01/line.png"><img class="aligncenter size-medium wp-image-187" title="line" src="http://studl.es/wp-content/uploads/2012/01/line-300x300.png" alt="" width="300" height="300" /></a>

If you put the NXT in the gray area, you can have a proportional steering function. Light gray means just a bit left, while dark gray means just a bit right.

Proportional, you say? Yes, we can just apply good old <a href="http://studl.es/2012/02/about-pid-control/">PID</a> again!

{% highlight plaintext %}
// Define to which ports the sensor and motors are connected
#define LIGHTSENSOR IN_1
#define LEFT OUT_C
#define RIGHT OUT_A

// Define constants to tweak the algorithm
#define kp 100
#define ki 5
#define kd 30
// And another one to scale the final value
#define scale 10

dseg segment

// Light sensor reading
light word

// target light
target word
high word
low word

// The current error
err sdword
// The previous error
errold sdword
// The integral, all accumulated errors
errint sdword
// The deriviate, the expected next error
errdiff sdword

// Final pid value
pid sdword

// Temporary variable for calculations
temp sdword
temp2 sdword

// power to the motors
leftpower sdword
rightpower sdword

dseg ends

thread main
  // Initialize the light sensor
  SetSensorColorRed(LIGHTSENSOR)

  // Get the time and start turning around
  gettick temp
  add temp temp 3000
  OnFwd(LEFT, 50)
  OnRev(RIGHT, 50)

  // get light sensor reading
  getin light LIGHTSENSOR ScaledValue

  // set high and low to that reading
  mov low light
  mov high light

Circle:
  // Get the light reading
  // if it is more than high, jump to Higher
  // if it is lower than low, jump to Lower
  getin light LIGHTSENSOR ScaledValue
  brcmp LT Lower light low
  brcmp GT Higher light high

  // else check if the time has passed
  // Jump to Done, else go back to Circle
  gettick temp2
  brcmp LT Done temp temp2
  jmp Circle

// set light to the new low
// jump back to Circle
Lower:
  mov low light
  jmp Circle

// set light to the new high
// jump back to Circle
Higher:
  mov high light
  jmp Circle

Done:
  // we now have the max and min light value found
  // calculate the center value
  sub target, high, low
  div target target 2
  add target target low

Forever:
  // Read the sensor and store it in light
  getin light LIGHTSENSOR ScaledValue

  // Substract the actual distance from the target for the current error
  sub err target light // Proportional

  // Add the error to the integral
  add errint errint err // Integral
  mul errint errint 0.8 // multiply by 0.8 to dampen it

  // Sunstract the previous error from error
  // so that we get the speed at which the error changes
  sub errdiff err errold // Derivative
  mov errold err // set the current error as he old error

  mul pid err kp // Apply proportional parameter

  mul temp errint ki // Apply integral parameter
  add pid pid temp

  mul temp errdiff kd // Apply derivative parameter
  add pid pid temp

  div pid, pid, scale       // Apply scale

  NumOut(0,0,target)
  NumOut(0,8,light)

  // saturate over 100 and under -100
  brcmp LT, under100, pid, 100
  mov pid, 100
under100:
  brcmp GT, overMin100, pid, -100
  mov pid, -100
overMin100:

  // subtract pid from one of the motors
  brtst LT, Negative, pid
  OnFwd(LEFT, 100)
  sub rightpower 100 pid
  OnFwd(RIGHT, rightpower)

  jmp Run
Negative:
  OnFwd(RIGHT, 100)
  add leftpower 100 pid
  OnFwd(LEFT, rightpower)
Run:

  jmp Forever
endt
{% endhighlight %}

Did you know that even the motors of the NXT use PID themselves to provide accurate control?