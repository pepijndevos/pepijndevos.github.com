---
layout: post
title: Incremental E-paper update
categories:
- epaper
- electronics
- arduino
- sparkfun
---

<iframe width="560" height="315" src="http://www.youtube.com/embed/oscur3i5V54" frameborder="0" allowfullscreen="allowfullscreen"> </iframe>

This is what I've been up to since last post. I almost completely rewrote the Arduino code to support different waveforms and both full updates and incremental ones.

I admit the old update looked smoother, but it faded away over time, defeating on of the main features of E-paper, that it is bi-stable.

I do think the incremental updates look cool, kind of like an evolving Pokémon.

[Get the source code](https://github.com/pepijndevos/arduino-epaper)

I am using a [modified version of shiftOut](https://github.com/pepijndevos/arduino-epaper/blob/master/ePaper.cpp#L300) that can send any number of bits. This is needed to send 1 bit for the background and common, and 16 bits for a character.

The EIO pin is no longer used, but just tied low. This causes the first chip to be selected when it wakes up. The enable pins are chained so that when the first chip is full, it pulls the second chip low.

In short, this is how I update the screen:

Step one is to convert all 8 bit characters to 16 bit segment data using a [big table](https://github.com/pepijndevos/arduino-epaper/blob/master/ePaper.cpp#L60).

Next, I create a struct with the changes. This struct contains 6 arrays. One with the added segments, one with the removed segments, one with all changed segments and 3 negatives.

In case this is a complete update, this struct is very easy to make. In this case the negative of the aded segments equals the deleted segments.

In the case of a partial update, this struct is generated using a [lot of binary logic](https://github.com/pepijndevos/arduino-epaper/blob/master/ePaper.cpp#L246), based on an array of the current segments.

Now that we have a struct of all the changes, we actually need to write this to the shift register and toggle the latch to send it to the display. The [writing itself](https://github.com/pepijndevos/arduino-epaper/blob/master/ePaper.cpp#L316) is fairly trivial, but the waveform is not.

I will cover the [simple waveform](https://github.com/pepijndevos/arduino-epaper/blob/master/ePaper.cpp#L401) in this post, and leave the 757 for you to figure out. The principle is the same.

First we [wakeup](https://github.com/pepijndevos/arduino-epaper/blob/master/ePaper.cpp#L349) the display. After every update, we shut it down again.

Now, you need to look at [the datasheet](/images/Winstar_E-Paper_Application_Note_V2.pdf) to see the waveform for segments to white and segments to black. Unchanged segments need to be the same as the common bit, so no current flows through them.

Note that for white-on-black the added/deleted bits are the other way around, but the unchanged segments aren't.

Finally, for every section for the waveform I take the corresponding array out of my changes, send it, and wait for a bit.