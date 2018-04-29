---
author: pepijndevos
comments: true
date: 2012-06-12 06:17:31+00:00
excerpt: None
layout: post
link: http://studl.es/2012/06/nxt-in-line-skater/
slug: nxt-in-line-skater
title: NXT in-line skater
wordpress_id: 352
categories:
- studl.es
- Building Instructions
- Robots
---

I found <a href="http://www.youtube.com/watch?v=-aR6pzzQoCo&amp;t=1m28s">a video</a> that features a skating robot, it moves, but skating? I did a lot of skating myself, so I started to think about a better skating movement.

It occurred to me that while skating around pylons, you don't need to take your skates of the ground. What you do instead is hard to explain.

I tried to explain my idea to anyone who'd listen in <a href="irc://irc.freenode.net/mindboards">#mindboards</a>, but to no avail. You just have to see it with your own eyes.

<iframe width="500" height="281" src="http://www.youtube.com/embed/rbyzNncWOCg" frameborder="0" allowfullscreen> </iframe>

It is a really fun thing to build and play around with. I suspect you can find more efficient and inexplicable ways to move and turn.
<h2><a href="https://www.dropbox.com/s/ec2be9n6qc09f9t/skater.pdf?dl=0"><strong>Get the building instructions for this model</strong></a></h2>
This is the Mirah code I used in the video:

{% highlight plaintext %}
import lejos.nxt.Motor
import lejos.nxt.Button

def align()
	Motor.A.rotate(360 - (Motor.A.getTachoCount() % 360))
	Motor.C.rotate(360 - (Motor.C.getTachoCount() % 360))
end

speed = 150
Motor.A.setSpeed(speed)
Motor.C.setSpeed(speed)

Motor.A.forward()
Motor.C.forward()

Thread.sleep(20000)

Motor.A.stop()
Motor.C.stop()

align()

Motor.A.backward()
Motor.C.forward()

Thread.sleep(20000)

align()

Motor.A.forward()
Motor.C.forward()

Button.waitForAnyPress()
{% endhighlight %}
