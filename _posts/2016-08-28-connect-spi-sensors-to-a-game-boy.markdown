---
layout: post
title: Connect SPI sensors to a Game Boy
categories:
 - gameboy
 - assembly
 - spi
---

<iframe width="500" height="315" src="https://www.youtube.com/embed/LyEFKb42uew" frameborder="0" allowfullscreen> </iframe>

In all my previous Game Boy hardware hacks, I always used an Arduino to talk to the Game Boy over the Game Link cable. But the Game Link protocol is essentially just SPI, so I was thinking it would be trivial to talk directly to SPI sensors.

So I ordered an [LIS3DH breakout](https://www.adafruit.com/product/2809) from Adafruit and connected it to the Game Link port of my Game Boy. I wrote some code to read the WHOAMI register of the sensor, which should return 0x33. Nothing happened. I double-checked all the code and wiring, but nothing worked.

On day two I probed around with my oscilloscope and dug around in the datasheet.
The problem turned out to be that I tied the Chip Select to ground, which is actually required to indicate the start of a transaction[^1]. But the Game Boy does not have a Chip Select pin, so that seemed like it would be the end of it.

On day three I figured I might be able to emulate the CS line somehow. I tried various things with low-pass filters, binary counters and even an Attiny25, but nothing quite worked. The highlight of the day was that the Game Boy would display 0x24 if the stars aligned, and random junk otherwise. I gave up again.

On day four I woke up with the winning solution. It was glorious.

![33](/images/gbstep/buffer.png)

A MOSFET buffer connected to the clock line pulls low the CS line and a capacitor combined with the 10k pull-up on the breakout keep the line low between clock pulses and release it after transmission.

![33](/images/gbstep/scope.png)

I have never before been so excited about the number 0x33. After 4 days of despair, trial and error, it finally worked.

![33](/images/gbstep/33.jpg)

It should be noted a large delay between transactions is required to raise the CS pin, so transmission will be slow.

After setting up the control registers somewhat correctly, I was able to read accelerometer data from the sensor. I made a quick demo that moves a dot around by rotating the sensor, as seen in the video. It's not hard to imagine you could easily make a tilting maze game out of this.

But I have other plans. To be continued.

[^1]: I checked various sensors, and every single accelerometer I found was dual I2C/SPI and bit-for-bit identical to the LIS3DH, so using another sensor was not an option.
