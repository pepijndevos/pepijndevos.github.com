---
layout: post
title: Introduction To Game Boy Hacking at SHA2017
categories:
- gameboy
- pokemon
- assembly
---

At SHA2017 I gave a workshop about Game Boy assembly programming.
Despite the projector not working, it was fun to do, and I got some nice feedback.
We looked at some code from Pokemon Red, made some small changes, and more.
Unfortunately there was not enough time to dive into Super Mario Land and write code from scratch.

For those that want to continue, or were not there, the PDF can be downloaded [here](/sha2017/workshop.pdf).

As a bonus, someone asked how you could disassemble Super Mario Land and extract images from it.
In theory this should be easy by using [pokemontools](https://github.com/pret/pokemon-reverse-engineering-tools), but I could not get it to work right away.

What I did do is search the code for the location of the sprites.
The VRAM tile data is located at 0x8000, so searching for that adress in the debugger gives a few places that copy data from ROM to VRAM.
Using pokemontools, I was able to at least decode a block of tiles at 0x4032, shown below.
Other blocks should be possible to find as well, but that's for another time.


![Super Mario Land tiles](/sha2017/tiles.png)
