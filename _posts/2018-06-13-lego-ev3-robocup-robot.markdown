---
layout: post
title: LEGO EV3 RoboCup Robot
categories:
- studl.es
- Robots
---

This is a story about how my curiosity led me to be conscripted into a student team.

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/A-2iLsdhLFc?rel=0" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen> </iframe>

One of my friends is a member of [RoboTeam Twente](http://roboteamtwente.nl/) and is currently in Canada with the rest of the team to compete in the RoboCup [Small Size League](http://wiki.robocup.org/Small_Size_League). They only started last year, and will be competing against teams that have been playing for decades. You can follow their progress live [here](https://www.facebook.com/roboteamtwente/)

Having worked non-stop on my bachelor thesis in Electrical Engineering, my hands were itching to do some programming. So I nudged my friend if I could maybe entertain myself with their code. It turns out a few weeks before the competition is not a good time to introduce new people to the codebase, but this initiated an avalanche of requests to please join the team of next year, full-time please, and we're looking for board members.

However, this left me with my immediate itch to program something. Since I already started thinking about ideas for their soccer robots, I figured I might as well build my own robot from LEGO to satisfy my itch, while they are having fun in Canada. Before I knew it, I had already ordered a set of omni-wheels and borrowed an orange golf ball from the team (on the condition that I'd join... oh well)

![RoboTeam robot](/images/robocup/roboteam.png)

The RoboTeam robot is a holonomic platform with 4 omni-wheels that allow it to move in any direction. It has two solenoids for kicking and "chipping" (kicking the ball in the air) powered by a 200V capacitor. It also has a "dribbler", which is a rotating bar that keeps the ball against the kicker while moving.

My first challenge was figuring out how to implement that with an EV3 with only 4 motor ports. I figured I could do with 3 omni-wheel, and ditch the chipper. That leaves one motor for both dribbling and shooting. I thought that maybe with some slip gears I could dribble going one way, and shoot going the other way. The most powerful shooting technique I could think of is to compress a spring with a worm wheel, and release it.

I went through several iterations of this crucial and complex part, trying to make it more compact and sturdy. I used a medium motor that connects to a worm wheel and to a normal gear that drives a perpendicular axle. The worm gear drives a slip gear to a crankshaft that pulls the kicker back and releases it. The perpendicular axle drives another slip gear that drives the dribble bar.

![Kicker detail](/images/robocup/kicker.jpg)
![Dribbler detail](/images/robocup/dribbler.jpg)

After being somewhat satisfied with the kicker/dribbler, I moved on to the rear wheel. This was fairly straightforward, making a sturdy housing for the omni-wheel. The only issue was finding a space for the motor. This ended up looking a bit tacky because I had to move the motor to the back because it got in the way of the side wheels.

![Rear wheel detail](/images/robocup/rearwheel.jpg)

It was surprisingly hard to design the side wheels. To obtain a proper holonomic platform, most people opt to construct an equilateral triangle, but this was not an option with the kicker in the mix. Instead I went for two 3:4:5 Pythagorean triangles on the sides of the body, giving me 36.8&deg; angles for the side wheels. Note that the side wheel is attached one unit higher than the rear wheel, because the arms attach one unit below the main frame.

![Rear wheel detail](/images/robocup/sidewheel.jpg)

Due to the crankshaft of the kicker, I had to place the side wheels quite far back. That in turn meant that the rear wheel motor had to move. This combined means that the robot is larger than would be legal in the SSL. I believe it would be possible to comply with the rules, but a complete redesign is needed. All I wanted is to have some fun and write some software, so I'll leave it at this.

![Whole robot](/images/robocup/legoteamtwente.jpg)

Now I can finally begin to write software. My initial plan was to use Lejos, as it has ready-made classes for holonomic robots. But after some struggling with a two year old "beta" release, I decided it'd be easier to just do the math in Python myself, and use the much more actively maintained ev3dev-lang-python.

As a start, I blatantly copied [this script](http://www.ev3dev.org/docs/tutorials/using-ps3-sixaxis/), and adapted it for my Xbox controller and robot. The math ended up only being a few lines of numpy, much easier than I expected.

Next thing I want to try is to use ROS, which is also used at the RoboTeam. There is *some* documentation about using it on the EV3, but not much. So that will be a steep learning curve.

{% highlight python %}
#!/usr/bin/env python3

import evdev
import ev3dev.auto as ev3
import threading
import numpy as np

## Initializing ##
print("Finding xbox controller...")
devices = [evdev.InputDevice(fn) for fn in evdev.list_devices()]
for device in devices:
    if device.name == 'Microsoft X-Box 360 pad':
        gamepad = device

angles = np.deg2rad([-36.8, -90, 36.8])
coef = np.array([np.sin(angles), np.cos(angles), [-1,1,1]]).T

speed = np.zeros(3)
kick = 0
running = True

class MotorThread(threading.Thread):
    def __init__(self):
        self.motor_left = ev3.LargeMotor(ev3.OUTPUT_A)
        self.motor_back = ev3.LargeMotor(ev3.OUTPUT_B)
        self.motor_right = ev3.LargeMotor(ev3.OUTPUT_D)
        self.motor_kick = ev3.MediumMotor(ev3.OUTPUT_C)
        threading.Thread.__init__(self)

    def run(self):
        print("Engine running!")
        while running:
            sp = coef.dot(speed)
            try:
                self.motor_left.run_forever(speed_sp=sp[0])
                self.motor_back.run_forever(speed_sp=sp[1])
                self.motor_right.run_forever(speed_sp=sp[2])
                self.motor_kick.run_direct(duty_cycle_sp=kick)
            except OSError:
                pass

        self.motor_left.stop()
        self.motor_back.stop()
        self.motor_right.stop()

motor_thread = MotorThread()
motor_thread.start()

for event in gamepad.read_loop():
    if event.type == 3:
        axis = evdev.ecodes.ABS[event.code]
        if axis == 'ABS_X':
            speed[0] = event.value/(2**15)*300
        if axis == 'ABS_Y':
            speed[1] = -event.value/(2**15)*300
        if axis == 'ABS_RX':
            speed[2] = event.value/(2**15)*50
        if axis == 'ABS_Z':
            kick = (event.value>10)*100
        if axis == 'ABS_RZ':
            kick = -(event.value>10)*100
    elif event.type == 1 and event.code == 307 and event.value == 1:
        running = False
        break
{% endhighlight %}
