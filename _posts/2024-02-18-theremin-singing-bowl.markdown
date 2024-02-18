---
layout: post
title: Theremin Singing Bowl
image: /images/Thierrymin_v3_schem.png
categories:
- electronics
- music
---

Bear with me while I tell you a bit about some cool music instruments.

A theremin is an electronic instrument that you play by moving your hands in the air. It uses an analog circuit with two tuned oscillators, one of which is modulated by the antenna impedance which is perturbed by the presence of your body in the near field. The slight frequency differences are fed into a mixer to produce an audible frequency.

A singing bowl is a very old insturment that is usually played by rotating a suede covered mallet around its outside rim. The stick-slip motion of the mallet excites vibration modes of which the nodes are pushed around by the mallet. Some more experimental musicians also play singing bowls by drawing a violin bow across the rim.

A hurdy-gurdy is a string instrument, which uses a hand-cranked wheel with rosin on it rather than a bow. It also has other unique features like drone strings and a keyboard on the neck.

So here is the idea: what if you used a theremin to drive a hurdy-gurdy wheel and use that to play a singing bowl? Magic theremin singing bowl!

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/DvJmAOT3pbM?si=RvBnf3VKHoUuQq2C" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen> </iframe>

For the theremin I built myself a copy of the [Thierrymin](http://www.thereminworld.com/Forums/T/29231/my-new-year-gift-to-tw-a-new-theremin-circuit) (using J113 jfets), which I fed into a comparator, stepper motor driver, and stepper motor. From there the only things I had to tweak were a few resistors to add a little hysteresis, and I played around with the microstepping pins to change the speed of the stepper motor. Here is a rough schematic:

![schematic](/images/Thierrymin_v3_schem.png)

That was the easy part. Then I did a bunch of experiments with different types of wheels. I tried rosin, suede, wood, plastic. I tried to spin the wheel on a static bowl, or to spin the bowl against a static baton. It kind of worked, but nothing worked quite the way I wanted it. And then life got rather busy and the project sat dormant for a while. So that's when I decided to just write a blogpost with my progress and shelve the project. Maybe I'll get back to it one day, or inspire someone else.