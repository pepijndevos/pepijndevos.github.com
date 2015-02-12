---
layout: post
title: Grep your way into Pokemon Red
categories:
- pokemon
- assembly
---

I’m trying to figure out how the serial communication between two first generation Pokemon games works.

Armed with a [CPU manual](http://marc.rawer.de/Gameboy/Docs/GBCPUman.pdf) and [opcode summary](http://gameboy.mongenel.com/dmg/opcodes.html), I’m grepping through a [disassembled ROM](https://github.com/iimarckus/pokered).

Reading the section in the CPU manual about serial communication, I found that it uses two registers at addresses `ff01` and `ff02`. Grepping the source reveals `constants/hardware_constants.asm` that defines `rSB` and `rSC`.

Grepping those leads me to `home/serial.asm` and `engine/overworld/cable_club_npc.asm`. I’m not sure which does what, but in the former I find a promising label called `Serial_ExchangeBytes`. Grepping for that leads me to `engine/cable_club.asm`.

The function is [called 3 times](https://github.com/iimarckus/pokered/blob/master/engine/cable_club.asm#L124-L139) in this file. Looking at the comments and at my opcode reference, it seems to be using `ld` to load 3 16 bit registers for the arguments. `hl` stores the data to send, `de` the data to receive, and `bc` the number of bytes to send.

Grepping for `wSerialRandomNumberListBlock` leads me to `wram.asm` where all 3 are defined. Looking in the manual, WRAM refers to the 8 4Kb RAM banks that can be mapped into certain address spaces. I’m assuming `ds n` means a data section of n bytes, but I’m not sure.

So the first command sends 17 bytes of random data, while `wSerialRandomNumberListBlock` is only 7, but followed by 10 bytes of `wLinkBattleRandomNumberList`. I could grep further, but it’s random data, so whatever. On to `wSerialPlayerDataBlock`

[This block](https://github.com/iimarckus/pokered/blob/master/wram.asm#L1360-L1385) contains all the good stuff. We know we are looking for 424 bytes of data, but here we see them clearly labeled.

First there is a preamble, like with the random block. No idea what it’s for. Then there is space for the player name, followed by the number of Pokemon you cary and their ID. `PARTY_LENGTH` is just defined as 6.

Then there are 6 `party_struct` that contain more info about your Pokemon. I know because I found [this page](http://bulbapedia.bulbagarden.net/wiki/Pokémon_data_structure_in_Generation_I) earlier.

I’m grepping for those bytes in the preamble. They seem to be used for a bunch of other things, completely unrelated to the player data.

I’m just randomly trying to read bit and pieces of the code and comparing with bytes coming out of the Game Boy. In the data I see a lot of `FD`, and in the code I found `SERIAL_PREAMBLE_BYTE` which so happens to be defined as `FD`. These seem to be for synchronisation only.

That’s it for now. There is plenty of stuff I don’t understand, but at least I know the structure of the data that is sent over the link cable.