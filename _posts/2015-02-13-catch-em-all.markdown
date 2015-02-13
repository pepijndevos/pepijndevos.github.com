---
layout: post
title: Catch 'em All
categories:
- pokemon
- arduino
---

<iframe width="560" height="315" src="https://www.youtube.com/embed/XlqMkfmCZfM" frameborder="0" allowfullscreen> </iframe>

Isn’t it frustrating that some of the Pokemon in the first generation Game Boy games are [exclusive to one of the games](http://bulbapedia.bulbagarden.net/wiki/Pokémon_Red_and_Blue_Versions#Version_exclusives)?
There is no way to get Oddish in Blue and no way to get Bellsprout in Red.

The intended solution is to trade Pokemon with friends to get the missing ones. There are even Pokemon like Graveler that [only evolve when traded](http://bulbapedia.bulbagarden.net/wiki/Category:Pokémon_that_evolve_through_trading).

Sadly, not a whole lot of people own a Game Boy, a Pokemon game, and a link cable. So I decided to get creative and trade Pokemon with my Arduino.

Needed parts:

* 1 x Game Boy (Color and Advanced work too)
* 2 x Pokemon cartridge (Red and Blue)
* 1 x Arduino board
* 1 x Link connector

The Game Boy communicates over what is essentially a 5v SPI bus that can act both as a master and a slave. At 8KHz it is slow enough to bit-bang, so it works on any 3 GPIO pins.

I salvaged a connector from a GBA wireless adapter, and hooked it up to 3 Arduino pins with 1KΩ series resistors to be sure. Because both ends can in theory drive the clock line, I don’t want to short them out.

     ___________
    |  6  4  2  |
     \_5__3__1_/   (at cable) 

Connect to the Arduino as follows:

 1. DNC
 2. Serial Out
 3. Serial In
 4. DNC
 5. Serial clock
 6. Ground

I read through the [Pokemon disassembly](https://github.com/iimarckus/pokered) in my [previous post](http://pepijndevos.nl/2015/02/12/grep-your-way-into-pokemon-red.html), so I won’t go into too much detail about the data transferred. [Here](https://github.com/iimarckus/pokered/blob/master/wram.asm#L1360-L1385) is the data structure that contains the Pokemon, if you are interested.

Long story short, I emulated the protocol on the Arduino, acting as a slave. So go [Check out the code](https://github.com/pepijndevos/arduino-boy), and **finally** catch ‘em all.

The Arduino has one Pokemon stored in its EEPROM, which it will trade with you. So even if you have one cartridge, you can trade Graveler with the Arduino and then trade it back to get a Golem.

If you have two cartridges, you can trade a Pokemon with the Arduino, swap the cartridge, and trade it back.

No Pokemon where harmed in the making of this program. Or, not that many anyway. One Weedle turned into Slowpoke and then into Missingno. Several other Pokemon are now nicknamed Pidgey, but are otherwise doing fine.

I think it’s now complete, allowing multiple trades in one session, including canceling trades. Don’t forget to reset the Arduino when you reset the Game Boy, or bad things will happen.
