---
layout: post
title: Lego NXT With a Gamepad
categories:
- lego
- pygame
- nxt
- joystick
---
<iframe width="420" height="315" src="http://www.youtube.com/embed/IECaiDxiMYQ" frameborder="0" allowfullscreen="allowfullscreen"> </iframe>

I built this robot a while back, but I can't stand the software that comes with the NXT, my own compiler isn't ready yet, and any other remote controls I found didn't work particularly well, if at all.

> If you want it done right, you have to do it yourself -- Bernd Paysan

So I threw together PyGame and nxt-python, and wrote my own little application to control the NXT. Notice that it looks very similar to [this PyMouse code](http://pepijndevos.nl/2010/04/control-the-mouse-with-a-joystick/index.html)

I have no building plans for the robot, it's the standard wheel base with my own pincer. I do have the code:

{% highlight python %}
{% include code/nxtjoy.py %}
{% endhighlight %}