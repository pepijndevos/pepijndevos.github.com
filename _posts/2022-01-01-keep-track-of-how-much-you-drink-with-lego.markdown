---
layout: post
title: Keep track of how much you drink with Lego
image: https://github.com/pepijndevos/gnome-water-status/raw/main/ldraw/scale_lc.png
categories:
-Building Instructions
-lego
-arduino
---

It's important to stay hydrated, but hard to know how much you are actually drinking.
Some number of liters or cups does not easily translate to absentmindedly sipping from an oddly sized mug.
There are health apps that let you manually track how much you eat and drink, but that is such a hassle.
When I'm in the zone, it's already hard to refill my cup, let alone enter it into some app.

So I made a smart cup holder that weighs my cup and keeps track of how much I drink.

<iframe width="560" height="315" src="https://www.youtube.com/embed/eixtwrICMIs" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen> </iframe>

This setup consists of the following parts:
* Lego cup holder ([building instructions](https://github.com/pepijndevos/gnome-water-status/raw/main/ldraw/scale_lpub3d.pdf))
* load cell ([TAL220](https://www.sparkfun.com/products/13329))
* load cell amplifier ([HX711](https://www.sparkfun.com/products/13879))
* Arduino-compatible board ([Teensy 3](https://www.pjrc.com/teensy/teensy31.html))

The basic idea is quite simple. Every time the cup is placed down, its weight is recorded. If the weight is less than the last recorded weight, the difference is assumed to be consumed by me, and added to the total.
The [Arduino code](https://github.com/pepijndevos/gnome-water-status/blob/main/watergauge/watergauge.ino) is indeed quite simple.

The only noteworthy part of the code was calibrating the load cell.
I did this simply by taking a measuring cup and pouring 100ml of water at a time into a cup.
Repeat a few times with a few different cups and determine the scale factor.

The cup holder had to go through several design iterations.
The first ones had either too much friction, inconsistent weight measurement, or just weren't structurally stable enough.
The load cell is not a Lego part, so the tricky part was making the cup rest *only* on the load cell.
I used a Technic Flex-System Hose inserted into Technic Plates, which just about align with the load cell screw holes.

Fun fact: a 1 x 2 Grille is slightly thicker than a 1 x 2 Tile.
Maybe I'm imagining things, but it seems to be the difference between a tight fit or not.
With normal tiles I had to insert some pieces of paper to keep it from flexing.

With the mechanical parts out of the way, I went on to displaying the information.
Maybe a 7-segment display would have been the obvious choice, but I only had one digit.
I do have a full colour LCD panel, but that seems overkill and distracting.

If it's going to sit on my desk and I'm going to power it from my computer's USB ports, I might as well display the information on my computer.
A little icon should be less obtrusive than a blinky LED thing.
I may have underestimated how easy it is to get USB serial data in my taskbar, but in the end it worked out.

I forked a Gnome extension that displays the status of wireless earbuds, which it reads from the log file of some daemon.
All I had to do was change the icons a bit and make it read form the TTY... right?
Well turns out reading a TTY from Gnome JavaScript is a bit painful because there don't seem to be any functions to configure it.
It would also randomly close and reset for some reason.
In the end I shelled out to `stty` to set up the TTY to not block and then just get the whole contents of it.
In the process I learned that you JS extension can definitely hang and crash your entire Gnome shell. Great design that.

So that's it, right? Throw the code on Github, job done. Well, except I decided that in the name of reproducibility, I should make building instructions for the Lego cup holder.
I used to sell Lego Mindstorms building instructions, so I've done it before... many years ago.
So I installed LeoCAD and started making the model.

![LDraw model](https://github.com/pepijndevos/gnome-water-status/raw/main/ldraw/scale_lc.png)

At first it went pretty well, but then I ran into two problems.
First of all, I spilt the model into a base and a cupholder submodel.
But when generating building instructions, LeoCAD just inserted the submodel as a part.
The second challenge was making the flexible hoses.

I tried to open the model with LPub3D, which did render the submodel instructions, but did not show the flexible hose correctly because LeoCAD uses a non-standard format for those.
Then I installed Bricklink Studio under Wine, and redid the flexible hose there.
For some reason Bricklink Studio did not render the parts list in the building instructions, so I ended up going back to LPub3D to render the final building instructions. Phew!