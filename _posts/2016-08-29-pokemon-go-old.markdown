---
layout: post
title: Pokemon GO Old
categories:
 - pokemon
 - gameboy
---

Pokemon GO for old '90 kids based on Pokemon Gold[^1].

<iframe width="560" height="315" src="https://www.youtube.com/embed/T-5NcJOQtjA" frameborder="0" allowfullscreen> </iframe>

When I did [Pokemon Gringo](/2016/07/21/pokemon-gringo.html) I used Pokemon Red with an Arduino, GPS, and external battery. It worked, but it was kind of unwieldy and unreliable.

This hack solves all those problems. It's based on Pokemon Crystal, and relies on a pedometer that is directly connected and powered by the Game Boy.

Unlike Pokemon Gringo, Pokemon GO Old features the complete Pokemon game-play in full sound and color, except you have to be physically walking to walk in the game.

This means you get to hatch eggs by walking! Assuming a game step equals one meter[^2], it'd take [1Km (Magikarp) to 10Km (Dratino)](http://www.psypokes.com/gsc/breeding.php) to hatch an egg.

I made one other small change to bring character selection up to date with current standards.

![choose your style](/images/gbstep/poke.gif)

In my [previous post](/2016/08/28/connect-spi-sensors-to-a-game-boy.html) I explained how I managed to connect the accelerometer directly to the Game Boy. From there it is relatively easy to use the accelerometer as a pedometer to control your movement in Pokemon GO Old.

[I configured the LIS3DH](https://github.com/pret/pokecrystal/blob/2471cf406e2ef7c983db691fa4bac7ce91dd7d18/home/init.asm#L167-L206) to generate an interrupt when acceleration exceeds 64mg. Then [I added a function](https://github.com/pret/pokecrystal/blob/2471cf406e2ef7c983db691fa4bac7ce91dd7d18/engine/player_movement.asm#L453-L465) that checks the interrupt register in the code that moves the character. Finally [I made the buttons sticky](https://github.com/pret/pokecrystal/blob/056f9378896589bd41dbc0aacc8a5e6e2fab548c/engine/player_movement.asm#L16-L20) so that you keep walking as long as you walk.

In case anyone wants to try it out, the complete [ROM](https://github.com/pepijndevos/pokecrystal/releases/download/1.0/pokecrystal.gbc) and [code](https://github.com/pepijndevos/pokecrystal/tree/pedometer) are on Github.
The hardware is super simple; except for the flash cart it's probably around $15 of components.

![ball of wires](/images/gbstep/LIS3DH.jpg)

 * [A flash cart](http://www.retrotowers.co.uk/gb-gameboy-usb-smart-card-64m) to store the modified ROM.
 * [Adafruit LIS3DH breakout](https://www.adafruit.com/product/2809)
 * Game Link cable
 * 2x BS170 MOSFET
 * 10k resistor
 * 1uF capacitor

The Game Link cable connects directly to the LIS3DH, but a small circuit is required to drive the CS line.

![buffer](/images/gbstep/buffer.png)

[^1]: Actually Crystal, but everything for the pun.
[^2]: You take about 2 game steps in a real step, but no steps during battles.

