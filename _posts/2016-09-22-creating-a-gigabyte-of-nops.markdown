---
layout: post
title: Creating a Gigabyte of NOPs
categories:
 - c
 - assembly
---

At the university we're learning about computer architecture.
The professor said that registers are used because reading from main memory is too slow.
But then he also said the program is stored in main memory.
How can the CPU ever execute an instruction every cycle if memory is so slow?

The answer appears to be caching.
The CPU can store parts of the program in the cache, and access it fast enough from there apparently.
But what if your program is bigger then the cache, or even bigger than memory?
I assume it will be limited by the throughput of the RAM/disk.
Let's find out.

As a baseline I created a loop that executes a few NOPs. Enough to neglect the loop overhead, but not so much they spill the cache.
I was very surprised to find that on my 2.2GHz i7, it executed 11.5GHz. Wat?
I thought I made an error in my math or my NOPs got optimized away, but this was not the case.

It turns out that my CPU has a turbo frequency of 3GHz and 4 execution units that can execute an (independent) instruction each.
3&times;4=12GHz of single-core performance. Not bad.

Now what if it does not fit in cache? Let's create a GB of NOPs.
This was not so easy and I used several "amplification" steps.
First I compiled a C file to assembly with a CPP macro that generated 100 NOPs.
Then I saved the NOPs in a separate file and used vim to "100dd10000p" create a million NOPs.
Then I used `cat` to concatenate 10 of those files, and then 10 of those, and then included it 10 times in the original assembly file.
Then I compiled with `gcc -pipe -Wall -O0 -o bin10 wrap.S`, which took a good number of minutes.

The resulting file still runs at a respectable 7GHz.
I was expecting much slower, but in retrospect this was to be expected
since the throughput of my DDR3 RAM is apparently 10GB/s.
Much higher than I thought it would be.

To really slow down the program, I would need to make it so big that it doesn't fit in RAM.
Seeing how hard it was and how long it took to make a 1GB binary, a 20GB binary would require a new technique.

The other option is to generate a lot of jumps so that the speed becomes limited by the latency of RAM rather than the throughput.
But again, generating a GB of jumps requires a completely new technique.

I'll leave it at this for now, as I've already spent quite some time and learned a few interesting things.
Now it doesn't seem so odd anymore that apparently for large codebases `-Os` generally performs better than `-O3`.
