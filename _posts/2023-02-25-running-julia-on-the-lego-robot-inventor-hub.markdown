---
layout: post
title: Running Julia on the Lego Robot Inventor Hub
categories:
- lego
- julia
---

Someone asked if anyone had any experience interfacing Lego Mindstorms with Julia, and I was like **this is right up my alley**, so I got a little bit very much nerdsniped into making this work.

You see, the sensible thing would be to just run Julia on the EV3 which runs a full Linux, but the *modern* approach would be to run Julia on the Robot Inventor Hub, which is an STM32 running a MicroPython firmware.

Here is the game plan. Lacking a MicroJulia implementation and the resources to write a completely custom firmware, we're going to compile Julia functions into MicroPython extension modules and run them on [PyBricks](https://github.com/pybricks/pybricks-micropython).

This way you can do all your fancy [ControlSystems](https://juliacontrol.github.io/ControlSystems.jl/stable/) stuff in Julia, compile it to machine code, and then write some Python glue for the sensors and motors.

We'll basically need to do 3 things

* Compile Julia into ARM machine code
* Link the machine code into an .mpy library
* Run said library on PyBricks

### Run native code on PyBricks

MicroPython has [this whole page](https://docs.micropython.org/en/latest/develop/natmod.html) about how to write native modules in C and compile them into .mpy files. This should be easy!

So I just copied their factorial example, changed `ARCH = armv7emsp`, and ran `make` to end up with a sweet native .mpy module. Cool now just upload it. Oh. Anyway after [some hacks](https://github.com/pybricks/pybricksdev/pull/52) I could use `pybricksdev run ble test.py` to upload a tiny test program:

```python
import factorial
print("hello", factorial.factorial(4))
```

Except it threw `ValueError: incompatible .mpy arch` at me no matter what I tried. A little digging later, I found that I needed to [enable](https://github.com/pybricks/pybricks-micropython/pull/149) `MICROPY_EMIT_THUMB`. But then I got a weird error in MicroPython, which I [hacked around](https://github.com/micropython/micropython/pull/10855) as well.

Then, a small victory: running C code! Now onto Julia.

### Compile Julia into ARM machine code

For the first part I used the amazing [AVRCompiler.jl](https://github.com/Seelengrab/AVRCompiler.jl) which ties into GPUCompiler.jl which ties into LLVM. Long story short, we can abuse the machinery Julia has for running code on the GPU for generating machine code for other architectures.

All I did was compile Julia from source[^1] while adding `ARM` to the supported architectures, and changed the target triple in AVRCompiler to the one I found by by copying the [CFLAGS from PyBricks](https://github.com/pybricks/pybricks-micropython/blob/830579b255b526779c1ec29c20dd4286bdff9080/bricks/_common/arm_none_eabi.mk#L130) and asking LLVM:

```
clang -print-effective-triple -mthumb -mtune=cortex-m4 -mcpu=cortex-m4 -mfpu=fpv4-sp-d16 -mfloat-abi=hard --target=arm-none-eabi
thumbv7em-none-unknown-eabihf
```

Once that is in place you can just do

```julia
using AVRCompiler

function factorial(x)
    if x == 0
        return 1
    end
    return x * factorial(x - 1)
end

obj = AVRCompiler.build_obj(factorial, (Int32,))
write("factorial.o", obj)

```

### Glueing it all together

So we have a way to run native .mpy modules, and we have a way to compile Julia functions to .o files. Now we just need some glue to link the object file into .mpy and make it accessible from MicroPython.

I basically took the factorial example, ripped out the `factorial_helper` and replaced it with `extern int julia_factorial(int);` and modified the Makefile to call my Julia script to generate the object file.

```makefile
# Location of top-level MicroPython directory
MPY_DIR = ../pybricks-micropython/micropython
# Name of module
MOD = factorial
# Source files (.c or .py)
SRC = wrapper.c
SRC_O = factorial.o
# Architecture to build for (x86, x64, armv6m, armv7m, xtensa, xtensawin)
ARCH = armv7emsp
# Include to get the rules for compiling and linking the module
include $(MPY_DIR)/py/dynruntime.mk
# Build .o from .jl source files
%.o: %.jl $(CONFIG_H) Makefile
	$(ECHO) "JL $<"
	julia --project=. $<
```

This was all looking really promising until I ran `make` and was greeted by

```
LinkError: factorial.o: undefined symbol: __aeabi_unwind_cpp_pr0
```

But, yolo, lemme just comment that out in `mpy_ld.py` and press on. This symbol appears to be related to exceptions and I don't really care.

```
$ pybricksdev run ble test.py
Searching for any hub with Pybricks service...
100%|████████████████████████████| 280/280 [00:00<00:00, 2.15kB/s]
hello 24
```

At last! Julia code running on the Lego hub! TODO: make cool demo

[^1]: I used the `release-1.9` branch of Julia for [several](https://github.com/JuliaLang/julia/issues/48698) [reasons](https://github.com/JuliaGPU/GPUCompiler.jl/issues/396)