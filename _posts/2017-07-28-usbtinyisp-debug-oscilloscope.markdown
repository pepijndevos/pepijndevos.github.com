---
layout: post
title: USBTinyISP debug oscilloscope
categories:
- avr
- arduino
- isp
- debugging
---

When I'm using a normal Arduino board I usually just do printf debugging. But when I design a PCB with an AVR chip on it, I don't always want to use serial.

Now if you have an expensive official AVR debugger, you can use debugwire with Atmel Studio, but that is a proprietary protocol that is not supported by avrdude and the USBTinyISP I have.

For my guitar tuner project I really needed a way to debug the program I'm writing. So I looked around if anyone had figured out something to do at least basic printf debugging via SPI.

The best thing I found is people using *another* Arduino to forward SPI commands to the Arduino serial console. That seemed a bit clumsy when I already have a perfectly fine SPI chip in my ISP.

So I took a break from my guitar tuner to write an SPI debugging tool. I configured the AVR as an SPI slave, and then used PyUSB to send SPI commands with the USBTinyISP. In this particular case it was more insightful to plot numerical values than to print text, so I used matplotlib to make a very basic "SPI oscilloscope" to probe some variables on the AVR.

![oscilloscope](/images/usbtinyispscope.png)

I put [the code on Github](https://github.com/pepijndevos/ISPdebug). It's pretty basic right now, but if people besides me actually need this kind of thing, it may eventually grow into a nice Arduino library and command line tool.
