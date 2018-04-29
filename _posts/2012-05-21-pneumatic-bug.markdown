---
author: pepijndevos
comments: true
date: 2012-05-21 09:51:34+00:00
excerpt: None
layout: post
link: http://studl.es/2012/05/pneumatic-bug/
slug: pneumatic-bug
title: Pneumatic Bug
wordpress_id: 315
categories:
- studl.es
- Building Instructions
- Robots
---

So there you are. This is the result of a day or two of LEGO building, followed by weeks of thinking, <a href="http://studl.es/2012/04/diy-servo-controlled-pneumatic-switches/">sawing</a> and <a href="http://studl.es/2012/05/diy-servo-controller/">soldering</a>. Lucky for you, you don't have to go through all that, and can just buy the needed parts.

This cute little bug can walk forwards, backwards and turn around, using 4 pumps on 3 switches. The outer 4 legs can move back and forth, with left and right having an individual pump. The middle legs are connected, and can move up and down.

By tilting the middle leg right, the outer right legs come of the ground, while the middle leg on the left is off the ground, then the outer legs can be repositioned for the next cycle.

There are touch sensors on the middle legs, to detect when they are of the ground.

<iframe width="500" height="281" src="http://www.youtube.com/embed/gC1m_B5TSVU" frameborder="0" allowfullscreen> </iframe>

To build this adorable model, you need your NXT, <a href="http://www.bricklink.com/catalogItem.asp?P=2793c01">4 pneumatic pumps</a>, <a href="http://www.bricklink.com/catalogItem.asp?P=x191c01">1 compressor pump</a>, <a href="http://www.mindsensors.com/index.php?module=pagemaster&amp;PAGE_user_op=view_page&amp;PAGE_id=141">3 servo valves</a> and a <a href="http://www.mindsensors.com/index.php?module=pagemaster&amp;PAGE_user_op=view_page&amp;PAGE_id=93">servo controller</a>.

This is the walking routine in NBC for my custom controller. The concept is the same for the Mindsensors one.

#define tilt 0
#define left 1
#define right 2
#define servoport IN_2

thread main
SetSensorLowspeed(servoport)
OnFwd(OUT_A, 100)

loop:
servo(servoport, left, 150, result)
servo(servoport, right, 150, result)
servo(servoport, tilt, 100, result)
wait 7000

servo(servoport, tilt, 150, result)
servo(servoport, left, 100, result)
servo(servoport, right, 200, result)

wait 5000

servo(servoport, left, 150, result)
servo(servoport, right, 150, result)
servo(servoport, tilt, 200, result)
wait 7000

servo(servoport, tilt, 150, result)
servo(servoport, left, 200, result)
servo(servoport, right, 100, result)
wait 5000

jmp loop

endt
<h2><a href="https://www.dropbox.com/s/4zyomjdbwg2ldca/bug.pdf?dl=0">Download building instructions</a></h2>