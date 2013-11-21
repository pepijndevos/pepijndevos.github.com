---
layout: post
title: Raspberry Pi mp3 player
categories:
- linux
- raspberrypi
- mp3
- cmus
---
![connected](/images/rplay/IMG_00000118.jpg)

Ingredients:
* Raspberry Pi
* USB battery
* Headphones
* Case(optional)
* Bunch of wires
* Female 1" header
* Pile of resistors
* Leftover shard of breadboard

Put the buttons, resistors and wires on the breadboard in a basic pull-up configuration.

![schematic](/images/rplay/IMG_00000115.jpg)

Use tin, glue and pushpins to connect everything together.

![inside](/images/rplay/IMG_00000117.jpg)

Admire.

![case](/images/rplay/IMG_00000116.jpg)

Install cmus and write some code to control it using the GPIO buttons.

{% highlight python %}
{% include code/play.py %}
{% endhighlight %}

Make sure the code runs at startup. 

{% highlight bash %}
# /etc/rc.local
su - pi -c "screen -d -m cmus"
sleep 5
python /home/pi/play.py >> /var/log/pyplay.log 
{% endhighlight %}

Enjoy!

![ssh interface](/images/rplay/cmus.png)

Oh, the audio is pretty bad. It might need an USB sound card.