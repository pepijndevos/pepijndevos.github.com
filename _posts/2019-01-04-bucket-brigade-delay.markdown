---
layout: post
title: Bucket Brigade Delay
categories:
- music
- electronics
---

So far in my quest to build a synthesizer, I've [controlled a Game Boy synth with a linear potentionmeter](/2018/12/25/making-a-new-music-instrument.html) and [made a pressure sensing circuit for the SoftPot](/2019/01/03/sensing-pressure-with-a-softpot.html). Today I want to make an analog delay using a bucket brigade device.

<iframe width="560" height="315" src="https://www.youtube.com/embed/6qj8CbiJTm4" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen> </iframe>

To do this, I used a [BL3207](https://www.banzaimusic.com/BL3207.html) bucket brigade device, and a [BL3102](https://www.banzaimusic.com/BL3102.html) clock generator, which are clones of the MN3207 and MN3102, respectively. The datasheets for the former are a single page and not of much use, but since they are clones, the original datasheets provide more useful information. Most importantly, this reference circuit.

![MN3207 reference schematic](/images/synth/mn3207circuit.png)

I did not build that exact circuit, but kind of used it as a hookup guide for the two chips. The MN3102 basically uses the RC filter to generate two opposing clock signals. It also generates a special voltage that's 15/16 of Vdd for some reason. The MN3207 then takes an analog input, and with some pull-down resistors, produces a delayed output signal. In the above circuit there is a lot of filtering going on, while my circuit uses a simple first order RC filter for now. Both the delayed output and the input signal are fed into an opamp adder to make a nice feedback loop. In the above video I'm simply turning the feedback gain.

![ball of wires](/images/synth/ballofwires.jpg)

To be honest, the current circuit sounds quite bad and is not very flexible. It just adds a fixed delay and loops it back on itself, going from inaudible to metallic to oscillating in the blink of an eye. The best sound I got out of it is the click track when it's almost oscillating and becomes this funny tone by itself. Maybe all of the filtering in the reference circuit make it a lot better, but I have some other ideas.

I have like 5 of these delay chips, so it'd be fun to chain them together for a longer delay. The other thing is the clock generator: You can disconnect the RC and drive the oscillator externally. I'm thinking I could create an external clock, and modulate that with an LFO to create a tremolo effect.
