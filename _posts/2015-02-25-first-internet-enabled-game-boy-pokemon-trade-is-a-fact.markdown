---
layout: post
title: First internet enabled Game Boy Pokemon trade is a fact
categories:
- pokemon
- gameboy
- teensy
---

<iframe width="560" height="315" src="https://www.youtube.com/embed/0X6RxmUK6jA" frameborder="0" allowfullscreen></iframe>

**IT WORKS!!!** I traded a Pokemon from one Game Boy to another over the internet.

Last week I blogged about [trading Pokemon with an Arduino](http://pepijndevos.nl/2015/02/13/catch-em-all.html), which made some people wonder if you could trade over the internet.

This is not a new idea, but one that was previously considered impossible. The Game Boy uses a synchronous protocol that does not tolerate the latency associated with the internet.

**But only when using its internal clock.** When using an external clock it will go as slow as you want. Normally one of the Game Boy’s drives the clock line. The solution therefore is to trick both Game Boy’s into thinking the other Game Boy is driving the clock line.

This trick is specific to the first and maybe second generation of Pokemon games, and it is a lot slower than a direct Link Cable, but it works nonetheless!

It’s not quite ready yet, so a post with more details is coming soon. Keep an eye on my [RSS](http://pepijndevos.nl/atom.xml), [Twitter](http://twitter.com/pepijndevos) or [Youtube](https://www.youtube.com/channel/UCsHqIpVIUniKlk1Wmr_Ag6w).

The gist of it is that a Teensy puts the Game Boy in slave mode and forwards the data over USB HID to a Chrome App that uses WebRTC to transfer the data over the internet.
