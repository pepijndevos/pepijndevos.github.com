---
layout: post
title: Sparkfun 1.8v USB to serial
categories:
- sparkfun
- usb
- serial
- kindle
---

![Sparkfun usb serial board with custom stripboard](/images/DSCN1878.JPG)

I am trying to hack my Kindle 4 so that I can plug in a keyboard and use it as a Raspberry Pi with battery and E-paper screen.

To support a keyboard, the Kindle needs to use its USB port in host mode. This is called USB On The Go, and someone modified the Kindle kernel to support this.

So far, it does not work on my Kindle, but since the Kindle does not display any kernel messages while booting, I had no idea what went wrong.

Thus, I had to open up the Kindle to attach a serial console. My brother is writing a guide about this at [http://jurriaandevos.nl](http://jurriaandevos.nl)

![kindle without back cover and with serial header](/images/DSCN1879.JPG)

The next hurdle was that the [Sparkfun USB-serial breakout](https://www.sparkfun.com/products/718) comes with a solder jumper for 5v and 3.3v, but the Kindle uses 1.8v.

Therefore, I devised this small stripboard that lets you select any of these three voltages with 2 jumpers.

With the top jumper removed, you can switch the lower jumper to either jumper Vcc or the 3.3v reference to the VCIO pin.

With the top jumper connected and the bottom one in the lowest position, you engage a voltage divider between 3.3v and gnd, resulting in half that voltage(1.65v) on VCIO.

Before this works, you should desolder the built-in jumper, and remove the LEDs.

The LEDs are powered from Vcc, so they leak current into the circuit. I measured 2.7v on VCIO with the LEDs still on.

The voltage divider was made using two 10k resistors, but anything between 1k and 10k should be fine, I've read.

I'm looking forward to blog about my Kindle 4 keyboard soon.