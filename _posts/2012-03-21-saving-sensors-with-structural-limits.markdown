---
author: pepijndevos
comments: true
date: 2012-03-21 12:00:04+00:00
excerpt: None
layout: post
link: http://studl.es/2012/03/saving-sensors-with-structural-limits/
slug: saving-sensors-with-structural-limits
title: Saving sensors with structural limits
wordpress_id: 147
categories:
- studl.es
- Tutorial
---

When building a robot with some sort of back-and-forth motion, such as a steering car or a robotic arm, you commonly see touch sensors at the end or center to easily move to that point.

However, the NXT motors have built-in rotation sensors, so with a bit more fiddling, you can get rid of most touch sensors in your system by using the structural limits of the model.

<iframe width="500" height="281" src="http://www.youtube.com/embed/GmpWt-NXp3Y" frameborder="0" allowfullscreen> </iframe>

The basic idea is that you move the motor slowly forwards until it doesn't go any further, record the tacho count, rotate backwards slowly until it stops, record the tacho count. Now you know the center point(the average of the two), and you can move to any point within the limits real quick.

In NXT-G this can very easily be done using the <a href="http://www.hitechnic.com/blog/uncategorized/pid-block/">PID block by HiTechnic</a>, but it does not give you the endpoints, which you can notice in the video.

In NXC there is a more powerful <a href="http://nxt-firmware.ni.fr.eu.org/changes/absolute_position_regulation/">absolute position regulation</a>, implemented at firmware level. Flexible, fast, precise, awesome.