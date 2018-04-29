---
author: pepijndevos
comments: true
date: 2012-02-09 16:07:27+00:00
excerpt: None
layout: post
link: http://studl.es/2012/02/about-pid-control/
slug: about-pid-control
title: About PID control
wordpress_id: 149
categories:
- studl.es
- Code
- Tutorial
---

I found this video on the blog of Xander Soldaat:

<iframe width="500" height="281" src="http://www.youtube.com/embed/7BDjZYGHupE" frameborder="0" allowfullscreen> </iframe>

Unfortunately, he does not show how to actually implement a PID controller, or how <a href="http://en.wikipedia.org/wiki/PID_controller#Loop_tuning">to tweak the values of the algorithm</a>, so I thought I'd show you how it's done.

For my robot, I chose the <a href="http://www.hitechnic.com/upload/TrikeBase_Instructions.pdf">trike base by HiTechnic</a>, because it is simple, and usable for my next program. The result:

<iframe width="500" height="281" src="http://www.youtube.com/embed/e5lJjUvP3hc" frameborder="0" allowfullscreen> </iframe>

If you are new to NBC, the main thing to remember that an action consist of a line, starting with the action, usually followed by the variable to store the result in, followed by other parameters.

{% highlight plaintext %}add result 1 2{% endhighlight %}

Another important concept are comments, which start with //. These are my notes about what the code does, to help you understand it.

If you want to know more about NBC, read <a href="http://bricxcc.sourceforge.net/nbc/doc/NBC_tutorial.pdf">this tutorial</a>.

{% highlight plaintext %}
// Define to which ports the sensor
// and motors are connected
#define ULTRASONICSENSOR IN_4
#define motors OUT_AC

// Define constants to tweak the algorithm
#define kp 50
#define ki 12
#define kd 2
// And another one to scale the final value
#define scale 10

// target distance in cm
#define target 30

// From here to dseg ends are variable declarations
dseg segment

// Ultrasonic sensor reading
distance word

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

dseg ends

// This is where the actual code starts
thread main
  // Initialize the ultrasonic sensor
  SetSensorUltrasonic(ULTRASONICSENSOR)

Forever:
  // Read the sensor and store it in distance
  ReadSensorUS(ULTRASONICSENSOR, distance)

  // Substract the actual distance
  // from the target for the current error
  sub err target distance // Proportional

  // Add the error to the integral
  add errint errint err // Integral
  mul errint errint 0.8 // multiply by 0.8 for damping

  // Sunstract the previous error from error
  // so that we get the speed
  // at which the error changes
  sub errdiff err errold // Derivative
  // set the current error as he old error
  mov errold err

  mul pid err kp // Apply proportional parameter

  mul temp errint ki // Apply integral parameter
  add pid pid temp

  mul temp errdiff kd // Apply derivative parameter
  add pid pid temp

  div pid, pid, scale       // Apply scale

  ClearScreen()
  NumOut(0,0,pid)
  NumOut(0,16,distance)

  // saturate over 100 and under -100
  brcmp LT, under100, pid, 100
  mov pid, 100
under100:
  brcmp GT, overMin100, pid, -100
  mov pid, -100
overMin100:

  // Turn the motors according to the scaled PID value.
  OnRev(motors, pid)
  jmp Forever
endt
{% endhighlight %}

If you have built a robot, and written the PID controller, the last thing you need to do is tweak the parameters on the lines that start with #define.

kp is multiplied by the proportial, this is where you start. Set the other two to zero, and this one to any value.

If the robot does not move, increase it. If the robot oscillates wildly, decrease it. Do this until it until it oscillates just a bit.

Now divide kp roughly in half, so that it does not oscillate, but stops to early. Now increase ki until it reaches the target as fast as needed. It will overshoot its target.

Finally, increase kd until it stops on target with as little oscillation as possible. You might need to go back and tweak the other parameters a bit.

Leave a comment if you have any questions.