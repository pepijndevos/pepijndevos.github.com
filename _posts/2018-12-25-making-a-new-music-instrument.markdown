---
layout: post
title: Making a new music instrument
categories:
- music
- electronics
- gameboy
- arduino
---

A while a go I sent a message to Martin from Wintergatan that I want to help with the second version of his Modulin that he briefly talked about during one of his Marble Machine X videos.

<iframe width="560" height="315" src="https://www.youtube.com/embed/mFfe4ZRQOH8" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen> </iframe>

He never replied, so I just decided to play around with my own ideas. The immediate goal is not so much to design a music instrument as it is to play around with touch input, digital signal processing, and analog filters.

For my first instrument I decided to build a chiptune violin by using a Game Boy as the synthesizer.

<iframe width="560" height="315" src="https://www.youtube.com/embed/u8hKwKUdUJI" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen> </iframe>

Step one was to figure out touch input. At first I wanted to do capacitive touch, but sensing position is not as easy as it's made out to be. Then I found these [SoftPot](http://www.spectrasymbol.com/product/softpot/) lineair potentiometers, which seem pretty good.  At first I thought I would need a separate sensor strip to sense pressure, but by pressing down, a small track section of the SoftPot is shorted, resulting in a smaller total resistance that can be measured.

After playing with the Game Boy sound registers in the simulator for a while, I made a new copy of GALP for all the boilerplate code, then I added the following snippet to read an address and a byte from the Game Link port and simply write it to hiram. This code basically allows changing any register at all, not just the sound ones. The full code lives [here](https://github.com/pepijndevos/gbpaint/tree/synth).

{% highlight nasm %}
; enable sound
  ld a,%10000000 ; enable
  ldh [rAUDENA],a
  ld a,$ff ; all channels
  ldh [rAUDTERM],a
  ld a,$77 ; max volume
  ldh [rAUDVOL],a

.loop

  call SerialTransfer
  ldh a,[rSB] ; new address
  ld c, a
  call SerialTransfer
  ldh a,[rSB] ; new data
  ld [$FF00+c],a ; write register
  jr .loop
  
SerialTransfer:
  ld a, $80 ; start external transfer
  ldh [rSC], a
.transferWait
  ldh a,[rSC]
  bit 7, a ; is transfer done?
  jr NZ, .transferWait
  ret
{% endhighlight %}

Then I connected the SoftPot to an Analog input of an Arduino with a 100k pull-down, and connected the Arduino SPI pins to the Game Link port. The code then simply reads the analog pin and sets the correct sound register on the Game Boy to play the corresponding note. The full code lives [here](https://gist.github.com/pepijndevos/83e5a96435a48966cdf88f4c325ac76b).

{% highlight C++ %}
int oldValue = 0;
void loop() {
      int sensorValue = analogRead(A9);
      bool lowValue = sensorValue < 1;
      bool oldLowValue = oldValue < 1;
      int outputValue = map(sensorValue, 0, 1023, 440, 1760);
      int oldOutputValue = map(oldValue, 0, 1023, 440, 1760);
      if(!lowValue && oldLowValue) { // rising edge
        ch2Length(63, 2);
        ch2Envelope(15, 0, 0);
        ch2Play(outputValue, 0, 1); 
      } else if (lowValue && !oldLowValue) { // falling edge
        ch2Envelope(15, 3, 0);
        ch2Play(oldOutputValue, 0, 1);
      } else if(!lowValue) { // high
        ch2Play(outputValue, 0, 0); 
      }
      oldValue = sensorValue; 
      delay(10);
}
{% endhighlight %}

The current code does not sense pressure and just uses a square wave channel on the Game Boy with an envelope on release. Next items on the list of things I want to try are sensing pressure and playing with some bucket brigade chips that have been sitting in my drawer.