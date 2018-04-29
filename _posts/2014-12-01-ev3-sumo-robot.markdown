---
author: pepijndevos
comments: true
date: 2014-12-01 10:59:01+00:00
excerpt: None
layout: post
link: http://studl.es/2014/12/ev3-sumo-robot/
slug: ev3-sumo-robot
title: EV3 Sumo Robot
wordpress_id: 531
categories:
- studl.es
- Code
- Robots
---

After a long period of silence, I finally made some things with my EV3. Last time the tools where not really ready, but now everything works great.

<iframe width="500" height="281" src="http://www.youtube.com/embed/eh7HGcAEd8Y" frameborder="0" allowfullscreen> </iframe>

I used ev3dev and python-ev3 to build this bulldozer. Watching sumo matches at LEGO World, I saw a few things that do and don't work.

A lot of people seem to go for blades and hammers, but unless you actually want to destroy the robot, they are useless. Treads sound like a good idea, but wheels have more grip in practice. Make sure the wheels don't fall off. Encased wheels are the best.

The goal is to push the other robot off the table, so the more powered rubber the better. Four wheel drive is better than rear wheel drive, but rear wheel drive is still much better than front wheel drive.

It's all about pushing. The only useful thing besides pushing forward is pushing up or sideways. Pushing up is the easiest, because you have the floor to back you up. It also pushes the opponent's wheels off the ground. This lead me to the following design.

[gallery columns="2" ids="534,532,533,535"]

I used two pairs of driven rear wheels on a pivot, so even if I'm lifted, all four remain in contact with the ground. They are geared down, which means I'm slow, but powerful. All the weight of the motors and EV3 is directly above the wheel, for maximum grip.

On the front there is a lift arm, using a worm wheel and a very solid construction. I went through several iterations, improving the strength each time. On the first iteration, it was to weak, but its wedge shape still lifted my opponent a little.

The code is quite simple. Push forward. When you reach the edge, turn around. When something hits the bumper, raise the arm.

{% highlight python %}
from ev3 import lego
import time
import select
import os

# Utility for waiting on the motor.
p = select.poll()
callbacks = {}

def poll():
    for fd, mask in p.poll(0):
        os.lseek(fd, 0, os.SEEK_SET)
        state = os.read(fd, 16)
        if state == &quot;idle\n&quot;:
            p.unregister(fd)
            os.close(fd)
            callbacks.pop(fd)()

def on_completion(motor, callback):
    fd = os.open(motor.sys_path + '/state', os.O_RDONLY)
    callbacks[fd] = callback
    os.read(fd, 16)
    p.register(fd, select.POLLPRI)

# Initiate all sensors and motors
l = lego.LargeMotor(&quot;A&quot;)
r = lego.LargeMotor(&quot;D&quot;)
lift = lego.MediumMotor()

dist = lego.InfraredSensor()
line = lego.ColorSensor()
touch = lego.TouchSensor()

# reset the motors
l.reset()
r.reset()
lift.reset()

try:
    # run forwards
    l.run_forever(100, regulation_mode=False)
    r.run_forever(100, regulation_mode=False)
    # main loop
    while True:
        poll()
        if touch.is_pushed and lift.state == &quot;idle&quot;:
            # we hit something, raise the arm!
            lift.run_position_limited(-3000, 1000)
            on_completion(lift, lambda: lift.run_position_limited(0, 1000))
        elif line.reflect &lt; 20 and l.duty_cycle &gt; 0 and r.duty_cycle &gt; 0:
            # we reached the edge, back up and scan for opponent
            l.run_time_limited(1000, -100)
            on_completion(l, lambda: l.run_forever(100))
            r.run_forever(-100)
        elif dist.prox &lt; 50:
            # found opponent, charge!!!
            r.run_forever(100)
            l.run_forever(100)
finally:
    # stop motors and lower arm
    l.stop()
    r.stop()
    lift.run_position_limited(0, 1000)
{% endhighlight %}