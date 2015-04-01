---
layout: post
title: TCPoke Beta
categories:
---

![TCPoke shield](/images/IMG_20150327_182633.jpg)

TCPoke is my project that connects Game Boys over the internet to battle and trade with first and second generation Pokemon games.

Today the last parts for the TCPoke shield arrived, enough parts to make 9 test boards. I plan to give these away to people who can contribute to the project.

### What’s included

* PCB
* Game Link Cable ([second generation](http://en.wikipedia.org/wiki/Game_Link_Cable#Second_generation))
* LED
* 330 Ohm resistor
* 3x 1K Ohm resistor
* 2x header pins

### What you need

* Game Boy (Pocket, Color, or Advance)
* Pokemon cartridge (Red, Blue, Yellow, Gold, Silver, or Crystal)
* Teensy 2.0
* A soldering iron (optional)

The software is compatible with the classic Game Boy and the Teensy 3.1, but the shield and Game Link Cable are not.

### How to get it

Send an email to myfullname at gmail, containing at least

* Your interest in the project
* Your proposed contribution
* Your shipping address

I will select the best contributions and send them one of the prototype kits free of charge.

If you’d like me to assemble the kit, or if you would like to make a donation, please let me know.

### State of the project and help needed

The most important thing works: You can connect to another player and trade first generation Pokemon to complete your Pokedex.

Trading on the second generation is in a “should work” stage, but needs testing.

Battle on the first generation works, but is buggy. Battle on the second generation does not work.

There is a partial connection state machine in the desktop client, but it is mostly a dumb proxy. Work in this area is needed for battle, UI feedback, and additional features.

The desktop client is both my first Chrome app and my first Angular app, and I’m not a designer either. So general improvement here is needed.

Once the basics are covered, I’m open to crazy ideas. A built-in Pokedex, ranked ladder games, connecting to an emulator, supporting other games, you name it.
