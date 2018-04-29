---
author: pepijndevos
comments: true
date: 2012-03-28 22:21:56+00:00
excerpt: None
layout: post
link: http://studl.es/2012/03/explorer-robot-without-sensors/
slug: explorer-robot-without-sensors
title: Explorer robot without sensors
wordpress_id: 243
categories:
- studl.es
- Code
- Tutorial
---

Usually the first robot you make when you get the NXT is the wheelbase with a bumper, you know, make it run into a wall, turn back and repeat.

To really get my point about saving sensors across, I made a robot like that <em>without any sensors</em>.

<iframe width="500" height="281" src="http://www.youtube.com/embed/3wgh0sdec9g" frameborder="0" allowfullscreen> </iframe>

It works by turning the motors in regulated mode(constant speed, varying power) and measuring the actual power applied. If the robot runs into a wall, the firmware will apply extra power to the motors to keep them turning. With some tweaking, you can even detect which wheel hit the wall.

The commented code:

{% highlight plaintext %}
// define 2 variables for containing the actual speed
dseg segment
  aaspeed byte
  caspeed byte
dseg ends

thread main
Start:
  // turn the motors on, regulated
  OnFwdReg(OUT_AC, 50, OUT_REGMODE_SPEED)
  // wait for the robot to accelerate
  // it will apply full power here
  wait 1000
Forever:
  // get the actual power used
  getout aaspeed OUT_A ActualSpeedField
  getout caspeed OUT_C ActualSpeedField

  // print the power to the screen
  NumOut(0, LCD_LINE1, aaspeed)
  NumOut(0, LCD_LINE2, caspeed)

  // if one of the motors uses more than 75 power
  // jump to either LResistance or RResistance
  brcmp GT LResistance aaspeed 75
  brcmp GT RResistance caspeed 75

  // repeat forever
  jmp Forever

LResistance:
  // reverse, turn right, jump to start
  OnRevReg(OUT_AC, 50, OUT_REGMODE_SPEED)
  wait 2000
  OnFwdReg(OUT_A, 50, OUT_REGMODE_SPEED)
  wait 500
  jmp Start

RResistance:
  // reverse, turn left, jump to start
  OnRevReg(OUT_AC, 50, OUT_REGMODE_SPEED)
  wait 2000
  OnFwdReg(OUT_C, 50, OUT_REGMODE_SPEED)
  wait 500
  jmp Start
endt
{% endhighlight %}