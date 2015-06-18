---
layout: post
title: Raspberry Pi autopilot, Part 1 of many
categories:
- raspberrypi
- arduino
- drone
---

![raspilot](/images/raspilot.jpg)

There is of course [Navio+](http://www.emlid.com) and [Ardupilot](http://ardupilot.com) and possibly many others, but what is the fun in that? I’m building my own.

It will be powered by a Raspberry Pi. The extra processing power is nice, but the main reason was that it’s easy to attach a 3G dongle and a camera module. I plan to write the software in Elixir, just because I can.

So far I connected a bunch of breakout boards and poked them for a bit. I have:

* [Adafruit Proto HAT](https://www.adafruit.com/product/2314) (Get the standoffs too)
* [Arduino Mini](http://www.arduino.cc/en/Main/ArduinoBoardMini)
* [I<sup>2</sup>C level shifter](https://www.adafruit.com/products/757)
* [I<sup>2</sup>C 9-DOF](https://www.adafruit.com/products/2021)
* [UART GPS](https://www.adafruit.com/products/746)
* [I<sup>2</sup>C altitude sensor](https://www.adafruit.com/products/1893)

I placed the Arduino to the left with PORTD facing down. You’ll see why this is important in a second. The GND and +5V rows line up such that if you insert 3 header pins in the bottom rows, you can plug servos into them.

The Arduino runs at 5V to drive the servos, so a level shifter is needed to talk to the Raspberry Pi, which runs at 3.3V.

I put the level shifter to the right, running the I<sup>2</sup>C wires through it to the Arduino. I used I<sup>2</sup>C because I can attach many more sensors to the same 2 wires, and I need the UART for the GPS board.

The main reason for not using a pre-made servo HAT is that I want servo *inputs* as well as outputs, to support switching back and forth between autonomous and manual control, or even something in between.

For now the plan is to power the Raspberry Pi, servos, and Arduino form the BEC(Battery Elimination Circuit) of the ESC(Electronic Speed Controller). But I might have to use a separate UBEC if it turns out the BEC I have is not powerful enough.

[The Arduino Sketch](https://gist.github.com/pepijndevos/03c35cc249f3edc7f8d7) so far is pretty simple. It uses the Wire library to talk to the Raspi, and the Servo library to talk to the servos. The only tricky bit is the pin change interrupt on PORTD to read the input from the receiver.

So I can now read the receiver and move the servos over I<sup>2</sup>C, as well as talk to all the other sensor boards. More later as I get some software working.