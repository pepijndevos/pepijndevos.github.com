---
author: pepijndevos
comments: true
date: 2012-05-14 21:18:37+00:00
excerpt: None
layout: post
link: http://studl.es/2012/05/diy-servo-controller/
slug: diy-servo-controller
title: DIY Servo Controller
wordpress_id: 301
categories:
- studl.es
---

This is another advanced home-made device, It took me weeks to do it. I was looking at the <a href="http://www.mindsensors.com/index.php?module=pagemaster&amp;PAGE_user_op=view_page&amp;PAGE_id=93">Mindsensors servo controller</a>, when Matt Allen casually mentioned you could <a href="http://mattallen37.wordpress.com/2011/05/13/nxt-servo-controller/">make them yourself</a>. Once I set out to make one, I wanted it to do a little bit more.

<iframe width="500" height="281" src="http://www.youtube.com/embed/9Vnl4-xTfhA" frameborder="0" allowfullscreen> </iframe>

I thought it would be nice if it could read input from an RC receiver as well. The final design has 2 readable inputs and 3 switchable ones, so you can directly control the servos.

<a href="http://studl.es/wp-content/uploads/2012/03/PICT0186.jpg"><img class="aligncenter size-medium wp-image-304" title="PICT0186" src="http://studl.es/wp-content/uploads/2012/03/PICT0186-300x209.jpg" alt="" width="300" height="209" /></a>

Starting in the top-left corner, going clockwise, there is the PICAXE programmer header, 3 servo outputs, the servo battery connector, the NXT connector, 2 readable inputs and finally 3 switchable inputs. The PICAXE can directly control the outputs, or route them straight to the 3 non-readable inputs, for remote control.

If you know what you're doing, this is the stripboard layout I designed. Use wisely, and at your own risk.

<a href="http://studl.es/wp-content/uploads/2012/03/Screen-Shot-2012-03-21-at-11.51.08-AM.png"><img class="aligncenter size-medium wp-image-305" title="Screen Shot 2012-03-21 at 11.51.08 AM" src="http://studl.es/wp-content/uploads/2012/03/Screen-Shot-2012-03-21-at-11.51.08-AM-300x254.png" alt="" width="300" height="254" /></a>

The code, more or less compatible with the Mindsensors quick mode.

{% highlight plaintext %}
#picaxe20x2
#no_data
'#define switching
setfreq m32

init:
table 0x00, ("V0.1")
table 0x08, ("pepijn")
table 0x10, ("servo")

for b20 = 0 to 0x17
	readtable b20, @ptrinc
next b20

symbol servin1 = w0
symbol servin2 = w1
symbol servpos = b4
symbol servpin = b5
symbol incontrol = b6

servo B.2,150
servo B.3,150
servo B.4,150
pullup %10100000
hi2csetup i2cslave, 0x02
setintflags %01000000,%01000000

main:
pulsin b.1, 1, servin1
pulsin b.0, 1, servin2
put 0x42, word servin1
put 0x44, word servin2

#ifdef switching
if servin1 &lt; 1200 then
	incontrol = 1
	high C.4
	high C.3
	high C.2
else
	incontrol = 0
	low C.4
	low C.3
	low C.2
endif
#else
incontrol = 1
high C.4
high C.3
high C.2
#endif

goto main

interrupt:
setintflags %01000000,%01000000
hi2cflag = 0
if incontrol = 1 then
	get hi2clast, servpos
	let b20 = hi2clast - 0x5A
	lookup b20,(B.2,B.3,B.4),servpin
	servopos servpin,servpos
endif
return
{% endhighlight %}
