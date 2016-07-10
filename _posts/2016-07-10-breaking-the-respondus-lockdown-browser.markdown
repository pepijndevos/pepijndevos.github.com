---
layout: post
title: Breaking the Respondus Lockdown Browser
categories:
 - security
 - java
---

At the University of Twente there was a pilot program for digital testing using Pearson MyLabsPlus in the Respondus Lockdown Browser. This browser is supposed to stop students from using their laptops for other things besides the test.

It does this by minimizing all applications, disabling a bunch of things and maximizing itself. It also forces you to shut down software such as Skype and Steam.

After the test, a friend mentioned he had received a notification from WhatsApp web during the test.
Surely enough, I could easily reproduce this with a few lines of JS using the notification API.

This got me thinking if you could show other things on top of Respondus Lockdown Browser.
I literally copied some code for making a transparent always-on-top window in Java, added a `Thread.sleep` (so it would open after I started Respondus) and I was in business.

A little more copy-pasting and a dozen lines of Swing and I had a transparent WolframAplha client sitting on top of my lockdown browser.

![WolframAlpha client on top of Respondus](/images/respondus.jpg)

I've become convinced Respondus is selling snake oil, pure snake oil. To drive the point home, another friend demonstrated you could run Respndus Lockdown Browser in a virtual machine.

![Respondus in a VM](/images/RespndusVM.png)

Time-line of events:

 * 10 June 2016
   * 13:45 - 15:45 Math exam
   * Intensive hacking
   * 23:08 University of Twente and Respondus notified
 * 12 June 2016, UT says they will bring it up with respondus

Respondus never even responded.

Below is the result of my copy-pasting. I didn't even bother to rename anything.

{% highlight java %}
{% include code/TranslucentWindowDemo.java %}
{% endhighlight %}

I'm not sure it's wise to release a "weaponized" version of my "exploit", but I think anyone who can figure out how to compile and run this code and its dependencies knows enough to do the copy-pasting.

