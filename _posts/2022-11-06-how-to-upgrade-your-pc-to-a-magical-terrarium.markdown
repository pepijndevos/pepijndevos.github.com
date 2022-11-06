---
layout: post
title: How to upgrade your PC to a magical terrarium
image: /images/terrarium/case.jpeg
categories:
 - art
---

When I built my PC I did not want any of that RGB stuff, but the case I landed on had a glass sidepanel and it would be a shame if everything wast just pitch black inside. So I decided to turn it into a magical terrarium.

![A PC case with a blue glowing dragonfly inside](/images/terrarium/case.jpeg)

Here is how I did it. The dragon fly is cut out of acrylic sheet on a laser cutter, which also etches the markings on the wings and tail. To do this I drew it in Inkscape using two different colours. In the laser cutter software you can then assign different profiles to each colour.

![the outline of a dragonfly in red and black](/images/terrarium/dragonfly.svg)

Then I used hot glue to insert an LED into the hole, which I covered up on the bottom with some black tape. I then cut one leg of the LED short and soldered a resistor to it. The value of the resistor depends on the voltage and the type of LED. You can of course calculate this but the YOLO way is to just [do what the shop says where you got them](https://www.tinytronics.nl/shop/en/components/leds/leds/blue-led-5mm-diffused).

To connect it I browsed the motherboard manual in search for a header with 5V, and in my case there was an addressable LED header of which I just left the data pin disconnected. I then routed the wire so that the dragonfly hangs nicely in the middle of the case.

![a motherboard with a wire running to a header and up through a hole in the case](/images/terrarium/ledheader.jpeg)

Here is how it looks with the case open.

![an acrylic dragonfly hanging in an open PC case](/images/terrarium/dragonfly.jpeg)