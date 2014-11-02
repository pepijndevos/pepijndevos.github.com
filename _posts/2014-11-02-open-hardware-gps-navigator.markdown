---
layout: post
title: Open Hardware GPS Navigator
categories:
- arduino
- gps
---

![Arduino GPS](/images/15667233196_de461033c3_o.jpg)

I stumbled upon Open Street Map a few times, and it seemed like a good idea, but when I actually need a map, I reach for Google Maps.

Until it occurred to me it would be great to have GPS navigation on my bicycle. There are a few companies like Garmin that make them, but from what I understand they are not tailored for cyclists.

I thought this would be a fun Arduino project. Buy parts, download map, write code, profit… right? The buying part was easy.

* [Arduino Mega](https://www.adafruit.com/product/191)
* [TFT shield](https://www.adafruit.com/products/1651)
* [GPS breakout](https://www.adafruit.com/product/746)

I choose the Mega because it has 8K of SRAM instead of 2K in the Uno. My research showed I’d need it. I did not get the GPS *shield* because the pins conflict with the TFT shield.

Downloading the map was also easy, but several GB of XML and a few KB of SRAM is not a good match. I decided the right thing to do is to convert the XML to an R-tree and store it in a binary file on the SD card on the back of the TFT shield.

I spent a lot of time learning Rust and R-trees at the same time.

R-trees are fairly intuitive on the surface. It is a tree of rectangles that contain smaller rectangles. So to find nodes within a certain rectangle, you just have to descend in the rectangles that overlap with your query.

To insert a node, you just descend in the node that is the best fit, enlarging it if needed. But at some point the node is full and needs to be split.

Splitting nodes is the hard part, and there are several ways to do it, of varying cost, complexity, and efficiency. How you spit determines how good your tree is. Mine is fairly dumb.

After I had a basic R-tree working, I used [Osmosis](http://wiki.openstreetmap.org/wiki/Osmosis) to load my map data into Postgres, from where I loaded it into my R-tree.

I recursively wrote the bounding box and offsets to child nodes to a file and put it on the SD card in the Mega.

This was an interesting moment where I was developing C and Rust in parallel. The C development had much less friction, a lot of hacks, and many occasions where I drew random pieces of memory to the screen.

After some more hacking and debugging, I had my first map on the screen! Only it took a few minutes to draw.

Studying my code and SdFat revealed a few things:

There where a lot of nodes where it would read each subnode from disk, check its bounds, and backtrack. I figured that if I stored the bounding rectangles on the parent node, I’d have to read a lot less.

SD cards read in blocks of 512 bytes. I made no particular effort to align my nodes. I think this could save some buffering.

So I formatted the SD card (mistake!) and adjusted the file format so that nodes are aligned at 512 blocks and contain the rectangles of their children. The result: It’s even slower! What happened?

I put some `millis()` in my code, and it turned out that `seek()` calls where taking over 3 seconds. I googled around and found that most operating systems suck at formatting SD cards.

I reformatted the card with the [official utility](https://www.sdcard.org/downloads/formatter_4/https://www.sdcard.org/downloads/formatter_4/) and seek times magically went down to 500ms. Better, but not good enough.

It turns out that the reason is that at the edge of every cluster(4KB), it needs to read the FAT header where the next cluster is, in case the file is fragmented. (Now you know why defragmenting speeds up your PC)

But knowing that my file is contiguous, I could use a low-level function to read raw blocks. This was a **major** improvement, and allows maps at low zoom levels to be drawn in under a second.

The current status is that it can draw a map based on your location, show your speed and some other metrics, and zoom in and out.

<iframe width="560" height="315" src="//www.youtube.com/embed/QvVO6pY9WNY" frameborder="0" allowfullscreen> </iframe>