---
layout: post
title: "Microrack: A Small Modular Synthesizer"
image: /images/synth/microrack1.jpg
categories:
-  music
- electronics
- arduino
- microrack
---

<iframe width="560" height="315" src="https://www.youtube.com/embed/dpeZgNTUX_U" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen> </iframe>


Inspired by the Modulin, I've been making my own synthesizer, starting with a [Game Boy violin](/2018/12/25/making-a-new-music-instrument.html), adding [pressure sensitivity](/2019/01/03/sensing-pressure-with-a-softpot.html), and adding [analog delay](/2019/01/04/bucket-brigade-delay.html).

Over the past weeks I've been thinking about how I want to connect everything together. I knew I wanted to make it modular, but also that it had to be small enough to become a portable instrument, and hopefully easy to prototype and not too expensive. So I came up with what I call Microrack, a compact mixed-signal bus that is electronically compatible with CV. I [typed up a rough description here](https://github.com/pepijndevos/microrack/). In short, it uses a bus with analog multiplexers for audio, and an I2C bus for control signals.

I started by making the power supply and base board. Ideally you'd have something more efficient and powerful, but I started with a simple half-wave rectifier into linear regulators. The I2C lines are exposed to an external Arduino board that will control the user interface and the digital bus. Here is a rough schematic. One thing that is regrettably absent is any sort of current limit or fuse.

[![power supply schematic](/images/synth/psu.png)](/images/synth/psu.png)

Then I started working on the first module. I decided to start a little drum machine based on a noise source, a filter, and an envelope generator. The drum machine was mostly driven by the idea to make white noise in discrete logic. The heart of this module is a linear feedback shift register, implemented with two 74HC595 shift registers and a 4030 quad XOR gate.

[![linear feedback shift register](/images/synth/LFSR-F16.svg)](https://en.wikipedia.org/wiki/Linear-feedback_shift_register)

The shift clock of the registers is driven by an atmega328p. The output clock of the last shift register is driven by a NOT-wired XOR gate to close the feedback loop. The output clock of the first shift register is driven by the atmega at a lower rate, to sample the noise. The outputs of the first shift register are fed to a R-R2 resistor ladder.

[![resistor ladder](/images/synth/R2r-ladder.png)](https://en.wikipedia.org/wiki/Resistor_ladder)

So by controlling the shift clock and the output clock, the bandwidth and randomness of the noise can be controlled. The DAC output is then fed into an opamp to translate from [0 5] V to [-5 +5] V, which is then output via the analog multiplexer. I'm pretty happy with the result.

![microrack](/images/synth/microrack1.jpg)

Except then I fried the atmega.
