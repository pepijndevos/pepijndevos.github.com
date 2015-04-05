---
layout: post
title: Where are my Pokebytes saved?
categories:
- pokemon
---

Welcome to another episode of me grepping through Pokered. This time to figure out how the game decides to show the Pokedex menu or not.

You get your Pokedex from Oak after delivering the parcel, so Oaks Lab seems like a good place to look for clues.

After some unfruitful searching through the script for [Oaks Lab](https://github.com/iimarckus/pokered/blob/master/scripts/oakslab.asm) I decided to approach it from the other way and look at the start menu. A wild guess:

    git grep -i startmenu

[Bingo!](https://github.com/iimarckus/pokered/blob/master/main.asm#L1073-L1075) Done? I think not.

We can trace back `wd74b` to Oaks Lab and our old friend [wram.asm](https://github.com/iimarckus/pokered/blob/master/wram.asm#L2066), but this time I want to know where it is in the save file.

There are no direct references to our byte in any saving related code, so again, let’s start from the other direction. Another wild guess:

    git grep -i save

After a lot of not so interesting files, I see some matches in engine/save.asm. What jumps out are calls to `CopyData` originating in [SaveSAVtoSRAM](https://github.com/iimarckus/pokered/blob/master/engine/save.asm#L271).

`CopyData` is easily found, and copies `bc` bytes from `hl` to `de`.

At the start of all of the three saving functions are a few lines about `SRAM_ENABLE` and `MBC1SRamBank`. This is probably about switching banks in the cartridge, so that seems like a good point to look at the Game Boy pandoc.

It has a [section on bank switching](http://problemkaputt.de/pandocs.htm#mbc3max2mbyteromandor32kbyteramandtimer), and I found elsewhere that Pokemon Red used the MBC3 type.

So as you can see, addresses 0xA000-0xBFFF can be mapped to 3 battery backed RAM banks for save data. This corresponds to the `de` addresses we see in the code.

So starting with `SaveSAVtoSRAM0` we can now try to figure out which piece of WRAM is copied to which SRAM location.

Here we see the second RAM bank is mapped to 0xA000, so writing to 0xA000 puts a byte in SRAM at 0x2000.

Next we see that 11 bytes of `wPlayerName` are written to 0xA598. To get the SRAM address, we subtract 0xA000 and add 0x2000, resulting in 0x2598.

We can verify this with the [Bulbapedia article about the save file](http://bulbapedia.bulbagarden.net/wiki/Save_data_structure_in_Generation_I#File_structure) and it is indeed correct. Sadly, our beloved `wd74b` is not in the article, so on with our search.

Since the file does not reference our address directly, the only way to figure this out is to check all the copies in wram.asm to see if they include our byte. But we’re in luck.

The next copy copies from `wPokedexOwned` to `W_NUMINBOX` with `wd74b` [right in the middle](https://github.com/iimarckus/pokered/blob/master/wram.asm#L1438-L2266). Now all we need is math to tell the location in SRAM.

`wPokedexOwned` is copied from 0xD2f7 to 0xA5A3, giving us 0x25A3 as the SRAM starting address. Now we add to that the offset of `wd74b`, to give us:

    0x25A3 + (0xD74B - 0xD2f7) = 0x29f7