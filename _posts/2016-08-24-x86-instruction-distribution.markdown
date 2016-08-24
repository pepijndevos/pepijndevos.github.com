---
layout: post
title: Does a compiler use all x86 instructions?
categories:
 - assembly
---

On the Z80 CPU there are so few registers and instructions that you can easily know and use them all and wish there were more of them. 
However, half of the time it feels like the only on you really use is `ld`.
I imagine that if `ld` took half the number of clock cycles, average code would run twice as fast.

In modern x86(_64) there are so many that I even wonder if my compiler knows and uses them all, and how often. To get an impression, I ran this one-liner:

    objdump -d /usr/bin/* | cut -f3 | grep -oE "^[a-z]+" | sort | uniq -c

Full output [here](https://gist.github.com/pepijndevos/c9ea860acb9cc41d6ed6bd1df7ca0605). In total I counted 411 different mnemonics, topped by 15891451 `mov` instructions and a very long tail of instructions that only occur once or twice.

There are 33% `mov` instructions. Combined with `callq`, `je`, and `lea` making up over half of all code.

![Opcode pie chart](/images/opcodepie.png)

Between the expected compare and jump instructions, "Load Effective Address", "eXclusive OR" and "No OPeration" surprised me. Of course `nop` is probably padding and `xor` is the [best way to zero a register](http://stackoverflow.com/questions/33666617/what-is-the-best-way-to-set-a-register-to-zero-in-x86-assembly-xor-mov-or-and), but I have no clue why there are so many `lea` everywhere.

[Nobody really seems to know how many x86 instructions there are](http://stackoverflow.com/questions/15922708/how-many-instructions-on-x86-today), but someone counted 678, meaning there are over 200 instructions that do not occur even once in all the code in my `/usr/bin`.

It would be interesting to break it down further in "normal" instructions, SIMD instructions, other optimizations, and special purpose instructions... if anyone can be bothered categorizing 600+ instructions.
