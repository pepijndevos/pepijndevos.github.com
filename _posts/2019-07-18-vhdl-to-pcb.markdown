---
layout: post
title: VHDL to PCB
image: "/images/IMG_20190718_090104345.jpg"
categories:
- vhdl
- yosys
---

When learning to program FPGAs using VHDL or Verilog, you also learn that these hardware description languages can be used to design ASICs (application specific integrated circuit). But this is only something big corporations with millions of dollars can afford, right? Even though I later learned it only costs thousands, not millions, to make an ASIC on an older process, it is still far away from hobby budgets.

I had been keeping an eye on Yosys, the open source HDL synthesis tool, which can apparently do ASIC by giving it a liberty file that specifies the logic cells your foundry supports. Meanwhile I also toyed with the idea of making a 7400 series computer, and I wondered if you could write a liberty file for 7400 chips. I had kind of dismissed the idea, but then ZirconiumX came along and did it.

<blockquote class="twitter-tweet" data-lang="en"><p lang="en" dir="ltr">I don&#39;t know if &quot;teaching a new dog old tricks&quot; is an idiom, but I managed to wrangle Yosys into synthesizing verilog into 74-series logic gates.</p>&mdash; Dan Ravensloft (@ZirconiumX) <a href="https://twitter.com/ZirconiumX/status/1140003607078744064?ref_src=twsrc%5Etfw">June 15, 2019</a></blockquote>
<script async src="https://platform.twitter.com/widgets.js" charset="utf-8"></script>

It suffices to say this revived my interest in the idea and a lively discussion and many pull requests followed. First some small changes, then simulations to verify the synthesized result is still correct, and finally a KiCad netlist generator.

You see, generating a Yosys netlist is nice, but eventually these 7400 chips have to end up on a PCB somehow. Normally you draw your schematic in Eeschema, generate a netlist, and import that to Pcbnew. But instead I used [skidl](https://github.com/xesscorp/skidl) to generate the netlist directly. Then all there is to do is add the inputs and outputs and run the autorouter (or do it manually of course).

I decided to do a proof-of-concept "application specific interconnected circuit", with the goal of making something fun in under 10 chips. (a Risc-V CPU currently sits at about 850) I settled on a fading PWM circuit to drive an LED. I manually added a 555-based clock, and ordered a PCB for a few bucks. A few weeks later, this was the result. It worked on the first try! This feeling is even more amazing than with software, and shows that as long as there are no compiler/library bugs or DRC errors, logic simulations are a good way to prove your PCB design.

<iframe width="560" height="315" src="https://www.youtube.com/embed/-1G_gXUzLPg" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

To follow along at home you need to install Yosys. A recent release might work, but it's [getting better every day](https://twitter.com/whitequark/status/1150829419323363328), so building from source is recommended. Then you can just git clone [74xx-liberty](https://github.com/ZirconiumX/74xx-liberty) and go. There are a number of Verilog programs in `benchmarks` in case you'd rather make [PicoRV32](https://github.com/cliffordwolf/picorv32) in 7400 chips.

```bash
cd stat
make pwmled.stat # synthesize and run stat
../ic_count.py pwmled.stat # count number of chips used
cd ../sim
make pwmled.vcd # synth to low-level verilog and simulate
gtkwave pwmled.vcd # show test bench results
cd ../kicad
make pwmled.net # generate kicad netlist
```

But this was all done in Verilog, so where is the VHDL, you might wonder. Well, Yosys does not really support VHDL yet, but Tristan Gingold is hard at work making [GHDL](https://github.com/ghdl/ghdl) synthesize VHDL as a Yosys plugin. I think this is very important work, so I've been contributing there as well. After some pull requests I was able to port the breathing LED to VHDL.

Getting VHDL to work in Yosys is a bit of effort. First you need to compile GHDL, which requires installing a recent version of GNAT. Then you need to install [ghdlsynth-beta](https://github.com/tgingold/ghdlsynth-beta) as a plugin, allowing you to run `yosys -m ghdl`. [My fork of 74xx-liberty](https://github.com/pepijndevos/74xx-liberty/tree/devel) contains additional make rules for doing the above synthesization for VHDL files, which does something like this before calling the 7400 synthesis script.

```bash
cd stat
ghdl -a ../benchmarks/pwmled.vhd # analyse VHDL file
yosys -m ghdl -p "ghdl pwmled; show" # load pwmled entity, show graph
```

![yosys dot graph](/images/yosys_pwmled.svg)

A huge thank you to all the people working tirelessly to make open source hardware design a reality. You're awesome!
