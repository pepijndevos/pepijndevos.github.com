---
layout: post
title: AVR Fixed-point in-place Fast Fourier Transform
categories:
---

In various web pages and projects across the web lives a mysterious file that does a FFT in C. There are several different versions modified by various people to do various things. I have no idea where it came form.

All I know is that this particular version by yours truly runs on Arduino and 8-bit AVR chips in general.

{% highlight c %}
{% include code/fix_fft.h %}
{% endhighlight %}

The licensing and versioning on this thing apparently works as follows: Add your name and date to the list of authors and post it in your corner of the web. That’s all.