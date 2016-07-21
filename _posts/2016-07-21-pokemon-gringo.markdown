---
layout: post
title: Pokemon Gringo
categories:
 - pokemon
 - arduino
---

> In Puerto Rico, folk etymology states that the word "gringo" originated from the English words "green" and "go" referring to the desire of some locals to have the U.S. military (who allegedly wore green uniforms) leave the island by telling them: "Green, go!" [^gringo]

As a long-time Pokemon fan, I got really excited when I heard about Pokemon Go.
But excitement quickly turned into disappointment as I found out the Android runtime on my Blackberry does not support the required API version.

So I took my Gameboy to play some good old Pokemon Red, when a thought struck me.
How hard would it be to add a GPS to [TCPoke](http://pepijndevos.nl/TCPoke/) and modify [pokered](https://github.com/pret/pokered) to allow TCPoke to initiate wild Pokemon battles?

I had previously read parts of pokered to understand the serial protocol I needed to make TCPoke, but I never actually wrote Z80 assembly. Luckily, it turned out to be relatively easy. After a few hours I already added an extra menu item to the Cable Club, and a few hours later the menu actually did something.

It's kind of glitchy and broken, but the [diff](https://github.com/pret/pokered/compare/master...pepijndevos:master) is surprisingly small.

![GBG simulation of Pokemon Gringo](/images/poke.gif)

Next up I modified Bill's Arduino state machine to recognize the new menu item and added some GPS code. Again, it took a few hours, but the [diff](https://github.com/pepijndevos/arduino-boy/compare/pokemon_go) is again relatively small. (Except for this huge chunk of Pokemon level and location data I extracted from pokered)

But at this point I only had the code running in GBG/Gambatte (which I even [modified](https://gist.github.com/pepijndevos/801b676be479cd2df4b8b4d236220b4e) to emulate BIll's Arduino), so I had to order a [flash cart](http://store.kitsch-bent.com/product/usb-64m-smart-card) to load the modified ROM onto my Gameboy. After camping on the doormat for a week, ignoring friends and family and only leaving the house outside delivery hours, today I finally received the cart and could begin testing on real hardware.

![First boot](/images/pokmongringo.jpg)

It almost worked on the first try! There were some small bugs in the Arduino code, which I could not test up to this point. Then I did a few rounds around the block, carrying my whole laptop+Teensy+Gameboy around for debugging. The first Pokemon I encountered made me almost jump into the air from excitement and surprise.

Pokemon Gringo really captures that feeling of surprise when you're happily trodding along when suddenly...

![GBG simulation of Pokemon Gringo](/images/wild.gif)

For the best experience I recommend turning up the volume all the way and physically walking though tall grass.

Here is a video of me walking to the supermarket with my new gear.

<iframe width="560" height="315" src="https://www.youtube.com/embed/GyA6K9bgucY" frameborder="0" allowfullscreen> </iframe>

[^gringo]: [Gringo - Wikipedia](https://en.wikipedia.org/wiki/Gringo#Folk_etymologies)
