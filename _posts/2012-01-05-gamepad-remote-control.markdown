---
author: pepijndevos
comments: true
date: 2012-01-05 16:04:03+00:00
excerpt: None
layout: post
link: http://studl.es/2012/01/gamepad-remote-control/
slug: gamepad-remote-control
title: Gamepad remote control
wordpress_id: 29
categories:
- studl.es
- Code
---

<iframe width="500" height="281" src="http://www.youtube.com/embed/IECaiDxiMYQ" frameborder="0" allowfullscreen> </iframe>

I had this pincer bot that I had not yet programmed, but using software remotes proved disappointing. With a few lines of code, I was able to use any gamepad or joystick to control the robot.

To use this code, you need to know how to execute commands on your computer. Then, do the following.
<ol>
	<li><a href="http://python.org/download/">Install Python</a> if you don't already have it.</li>
	<li><a href="http://www.pygame.org/install.html">Install PyGame</a> for interfacing with the gamepad.</li>
	<li><a href="http://code.google.com/p/nxt-python/wiki/Installation">Install NXT-python</a> for interfacing with the NXT.</li>
	<li>Make sure your NXT and gamepad are connected and working.</li>
	<li>Run{% highlight plaintext %}python nxtjoy.py{% endhighlight %}

in the directory where you've downloaded the code below.</li>
</ol>
<div>The code assumes you have the pincer bot in the video, for which I'll give you instructions later. Moving around should work with most tank-steered robots.</div>
{% highlight python %}
import pygame
from nxt import locator, motor
from time import sleep

# edit this to reflect your joystick axis and buttons
axis = {'x':0, 'y':1}

b = locator.find_one_brick()

left = motor.Motor(b, motor.PORT_B)
right = motor.Motor(b, motor.PORT_A)
action = motor.Motor(b, motor.PORT_C)

closed = False

def limit(nr):
    if nr &gt; 50 or nr &lt; -50:
        return min(127, max(-128, nr))
    else:
        return 0

def move(fwd=0, turn=0):
    lp = int((fwd - turn) * -100)
    rp = int((fwd + turn) * -100)
    left.run(limit(lp))
    right.run(limit(rp))

def pincer(button):
    global closed
    try:
        if button and not closed:
            closed = True
            action.turn(-40, 70, emulate=False)
        elif not button and closed:
            closed = False
            action.turn(30, 70, emulate=False, brake=False)
    except motor.BlockedException:
        print action.get_tacho()

pygame.init()
j = pygame.joystick.Joystick(0) # first joystick
j.init()
print 'Initialized Joystick : %s' % j.get_name()
try:
    while True:
        pygame.event.pump()
        sleep(0.1)

        # get_axis returns a value between -1 and 1
        move(j.get_axis(axis['y']), j.get_axis(axis['x']))
        pincer(j.get_button(0))

except KeyboardInterrupt:
    j.quit()
{% endhighlight %}