---
layout: post
title: Vacuum Balloon
categories:
- physics
- vacuum
---

You know these helium balloons? They float because helium is lighter, less dense then air. You know what is even lighter than air? No air at all!

I came across the idea on [this website][1], where he notes

> I haven't done any math, but I assume this isn't feasible at the present time.

I *have* done some math, or rather, asked Wolfram Alpha.

So, first of all, how heavy is a cubic meter of air anyaway?

![weight of 1 cubic meter air](/images/wolframalpha-20111109043749224.gif)

Ok, so our balloon can weigh just over a kilo and still float. But what is the surface area of a balloon of that size?

![surface area of a sphere of 1 cubic meter volume](/images/wolframalpha-20111109044344634.gif)

Almost five square meter. So with one kilo of matter, we need to make a balloon of nearly 5 square meter surface area.

![volume of 1280 g Al / 4.836 square meter](/images/wolframalpha-20111109043833019.gif)

Aww, not looking good. If we use aluminum[^2], we can make a shell of a tenth of a millimeter thick. This shell will have to resist 1 bar, which equals 10 newton per square centimeter.

![pressure vessel formula](/images/wolframalpha-20111109055834761.gif)

I just [picked this up from Wikipedia][3], but presumably this means the stress on the shell will be 3162 newton per square millimeter.

Now, remember that this is a vacuum, so while a pressurized balloon retains it shape *because of* the pressure, the biggest problem with our vacuum balloon is the buckling force.

The smallest dent, a gust of wind, or even gravity itself, will cause the balloon to implode. I asked my dad[^4], and he asked his engineer, how to calculate this force. Third order magic.

[1]: http://keithwiley.com/mindWanderings/vacuumBalloon.shtml
[^2]: We should be using carbon or some composite probably, but it doesn't really get much beyond 1mm anyway.
[3]: http://en.wikipedia.org/wiki/Pressure_vessel#Stress_in_thin-walled_pressure_vessels
[^4]: He's an architect. 