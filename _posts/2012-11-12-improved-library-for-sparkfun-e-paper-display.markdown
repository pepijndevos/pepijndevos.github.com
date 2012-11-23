---
layout: post
title: Improved library for Sparkfun E-paper display
categories:
- epaper
- electronics
- arduino
- sparkfun
---

<iframe width="420" height="600" src="http://www.youtube.com/embed/_jO2olSo13Y" frameborder="0" allowfullscreen="allowfullscreen"> </iframe>

I got my E-paper display from Sparkfun, but I was disappointed by the quality of the library.

My version has more and nicer looking(IMO) characters, uses black text on white background, uses sleep mode, and most importantly, **no ghosting**.

What remains to be done is implementing temperature awareness. The colder the display, the longer it takes to update. The Atmega chip has a sensor built in.

[Original library](http://bildr.org/2011/06/epaper-arduino/)  
[My updates](https://github.com/pepijndevos/arduino-epaper)  

Thanks to the people at [Hack42](https://hack42.nl/) for helping me out with my bootloader and understanding the E-paper datasheet.

One little byte of information that I found particularly interesting is that the E-paper controller is basically a [shift register](http://en.wikipedia.org/wiki/Shift_register) with an extra latch. Maybe the code can be simplified using shiftOut?

From the library

> // Second ePrint removes the un-used segments - I know, this is weird...

I thought the same, until I understood the COM(mon) bit. This indicates that all specified segments should be pulled either high or low. But not both at the same time.

This means that the first pass, you put +35v on all the segments  you want to use, and the second pass -35v on the ones you don't want to use. Order doesn't matter, but gives a different intermediate state.
