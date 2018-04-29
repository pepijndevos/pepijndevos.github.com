---
author: pepijndevos
comments: true
date: 2012-03-07 02:35:43+00:00
excerpt: None
layout: post
link: http://studl.es/2012/03/chocolate-dispenser/
slug: chocolate-dispenser
title: Chocolate dispenser
wordpress_id: 204
categories:
- studl.es
- Building Instructions
---

The latest in chocolate breaking technology! Using patented breaker technology, bar after bar is transported and broken off. No animals where harmed in the making of this robot.

My father eats a lot of chocolate, so the original idea for this robot was for it to keep track of and limit your chocolate eating. Unfortunately the NXT doesn't keep track of the time, so you could just restart the program and eat more.

A solution to this problem would be to use the <a href="http://www.mindsensors.com/index.php?module=pagemaster&amp;PAGE_user_op=view_page&amp;PAGE_id=77">Mindsensors realtime clock</a>, which costs $20, but since I have no intention to actually keep this robot around, I just used it as a dispenser for the weak and lazy.

<iframe width="500" height="281" src="http://www.youtube.com/embed/qjL_tdJgI8c" frameborder="0" allowfullscreen> </iframe>

Chocolate is fed into the back of the robot and is then transported to the front. A light sensor detects the foil and aligns the chocolate to the front edge. I keep the foil around the bar to make detection easy and to keep my LEGO clean.

When the button is pushed, one bar is extended over the edge and broken off by 2 NXT motors. Check the NBC code:

{% highlight plaintext %}
#define BLOCKWIDTH 100

dseg segment
  button byte
  light word
dseg ends

thread main
  SetSensorColorFull(IN_1)
  wait 100

  OnRev(OUT_A, 50)
NotThereYet:
  ReadSensor(IN_1,light)
  brcmp EQ, NotThereYet, light, INPUT_BLACKCOLOR

  Off(OUT_A)
  RotateMotor(OUT_A, 50, -100)

StandBy:
  GetButtonState(BTNCENTER, button)
  brtst EQ StandBy button

  RotateMotor(OUT_A, 50, -##BLOCKWIDTH)

  OnFwd(OUT_BC, 100)
  wait 1000
  OnRev(OUT_BC, 50)
  wait 1000
  Off(OUT_BC)

  jmp StandBy
endt
{% endhighlight %}

It took some time to calibrate the machine, but it was delicious. No extra parts are required for this robot, except some chocolate.
<h2><a href="https://www.dropbox.com/s/mdsoft2kvkzqvj6/chocosafe.pdf?dl=0">Download building instructions</a></h2>