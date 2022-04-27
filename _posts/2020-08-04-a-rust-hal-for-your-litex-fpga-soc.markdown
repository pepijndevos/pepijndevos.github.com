---
layout: post
title: A Rust HAL for your LiteX FPGA SoC
image: /images/ulx3s_oled.gif
categories:
- rust
- fpga
---

[last updated on 27/04/2022]

![ULX3S demo](/images/ulx3s_oled.gif)
 - [example](https://github.com/pepijndevos/rust-litex-example)
 - [HAL](https://github.com/pepijndevos/rust-litex-hal)

FPGAs are amazing in their versatility, but can be a real chore when you have to map out a giant state machine just to talk to some chip over SPI. For such cases, nothing beats just downloading an Arduino library and quickly hacking some example code. Or would there be a way to combine the versatility of an FPGA with the ease of Arduino libraries? That is the question I want to explore in this post.

Of course you can use an [f32c softcore on your FPGA as an Arduino](http://www.nxlab.fer.hr/fpgarduino/), but that's a precompiled core, and basically doesn't give you the ability to use your FPGA powers. Or you can build your own SoC with custom HDL components, but then you're back to bare-metal programming.

Unless you can tap into an existing library ecosystem by writing a hardware abstraction layer for your SoC. And that is exactly what I've done by writing a Rust [embedded HAL](https://docs.rs/embedded-hal/latest/embedded_hal/index.html) crate that works for any [LiteX](https://github.com/enjoy-digital/litex) SoC!

LiteX allows you to assemble a SoC by connecting various components to a common Wishbone bus. It supports various RISC-V CPU's (and more), and has a library of useful components such as GPIO and SPI, but also USB and Ethernet. These all get memory-mapped and can be accessed via the Wishbone bus by the CPU and other components.

The amazing thing is that LiteX can generate an SVD file for the SoC, which contains all the registers of the components you added to the SoC. This means that you can use [svd2rust](https://docs.rs/svd2rust/latest/svd2rust/index.html) to compile this SVD file into a peripheral access crate.

This PAC crate abstracts away memory addresses, and since the peripherals themselves are reusable components, it is possible to build a generic HAL crate on top of it that supports a certain LiteX peripheral in any SoC that uses it. Once the embedded HAL traits are implemented, you can use these LiteX peripherals with every existing Rust crate.

The first step is to [install LiteX](https://github.com/enjoy-digital/litex#quick-start-guide) I'm installing into a virtualenv to keep my system clean. While we're going to use Rust, gcc is still needed for compiling the LiteX BIOS and for some `objcopy` action.

```bash
#rustup default beta
virtualenv env
source env/bin/activate
wget https://raw.githubusercontent.com/enjoy-digital/litex/master/litex_setup.py
chmod +x litex_setup.py
./litex_setup.py --init --install
./litex_setup.py --gcc=riscv
export PATH=$PATH:$(echo $PWD/riscv64-*/bin/):~/.cargo/bin

```

Now we need to make some decisions about which FPGA board and CPU we're going to use. I'm going to be using my ULX3S, but LiteX supports many FPGA boards out of the box, and others can of course be added. For the CPU we have to pay careful attention to match it with an architecture that [Rust supports](https://forge.rust-lang.org/release/platform-support.html). For example [Vexrisc supports](https://github.com/enjoy-digital/litex/blob/665367fe67d8985d3d0e327c790bfa9dee5e0e0a/litex/soc/cores/cpu/vexriscv/core.py#L48-L73) the `im` feature set by default, which is not a supported Rust target, but it also supports an `i` and `imac` variant, both of which Rust supports. PicoRV32 only supports `i` or `im`, so can only be used in combination with the Rust `i` target.

So let's go ahead and make one of those. I'm going with the Vexrisc `imac` variant, but on a small iCE40 you might want to try the PicoRV32 (or even Serv) to save some space. Of course substitute the correct FPGA and SDRAM module on your board.

VexRisc:
```bash
cd litex-boards/litex_boards/targets
python radiona_ulx3s.py --cpu-type vexriscv --cpu-variant imac --device LFE5U-85F --sdram-module AS4C32M16 --csr-svd ulx3s.svd --build --load
rustup target add riscv32imac-unknown-none-elf
```

PicoRV32:
```bash
python radioana_ulx3s.py --cpu-type picorv32 --cpu-variant minimal --device LFE5U-85F --sdram-module AS4C32M16 --csr-svd ulx3s.svd --build --load
rustup target add riscv32i-unknown-none-elf
```

Most parameters should be obvious. `--csr-svd ulx3s.svd` tells LiteX to generate an SVD file for your SoC. You can omit `--build` and `--load` and manually do these steps by going to the `build/ulx3s/gateware/` folder and running `build_ulx3s.sh`. I also prefer to use the awesome [openFPGALoader](https://github.com/trabucayre/openFPGALoader) rather than the funky `ujprog` with a sweet `openFPGALoader --board ulx3s ulx3s.bit`.

Now it is time to generate the PAC crate with `svd2rust`. This crate is completely unique to your SoC, so there is no point in sharing it. As long as the HAL crate can find it you're good. Follow [these instructions](https://docs.rs/svd2rust/latest/svd2rust/index.html#other-targets) to create a `Cargo.toml` with the right dependencies. In my experience you may want to update the version numbers a bit. I had to use the latest `riscv` and `riscv-rt` to make stuff work, but keep the other versions to not break the PAC crate.

```bash
cargo new --lib litex-pac
cd litex-pac/src
svd2rust -i ulx3s.svd --target riscv
cd ..
vim Cargo.toml
```

Now we can use [these instructions](https://docs.rs/riscv-rt/latest/riscv_rt/index.html) to create our first Rust app that uses the PAC crate. I pushed my finished example [to this repo](https://github.com/pepijndevos/rust-litex-example). First create the app as usual, and add dependencies. You can refer to the PAC crate as follows.

```toml
litex-pac = { path = "../litex-pac", features = ["rt"]}
```

Then you need to create a linker script that tells the Rust compiler where to put stuff. Luckily LiteX generated the important parts for us, and we only have to define the correct `REGION_ALIAS` expressions. Since we will be using the BIOS, all our code will get loaded in `main_ram`, so I set all my aliases to that. It is possible to load code in other regions, but my attempts to put the stack in SRAM failed horribly when the stack grew too large, so better start with something safe and then experiment.

```ld
REGION_ALIAS("REGION_TEXT", main_ram);
REGION_ALIAS("REGION_RODATA", main_ram);
REGION_ALIAS("REGION_DATA", main_ram);
REGION_ALIAS("REGION_BSS", main_ram);
REGION_ALIAS("REGION_HEAP", main_ram);
REGION_ALIAS("REGION_STACK", main_ram);
```

Next, you need to actually tell the compiler about your architecture and linker scripts. This is done with the `.cargo/config` file. This should match the Rust target you installed, so be mindful if you are not using `imac`. Note the `regions.ld` file that LiteX generated, we'll get to that in the next step.

```toml
[target.riscv32imac-unknown-none-elf]
runner = ".cargo/flash.sh"
rustflags = [
  "-C", "link-arg=-Tregions.ld",
  "-C", "link-arg=-Tmemory.x",
  "-C", "link-arg=-Tlink.x",
]

[build]
target = "riscv32imac-unknown-none-elf"
```

Then create `flash.sh` with the following content, which allows `cargo run` to upload the binary.

```bash
#!/usr/bin/env bash
set -e
# Create bin file
riscv64-elf-objcopy $1 -O binary $1.bin
# Program FPGA
litex_term --kernel litex-example.bin /dev/ttyUSB0
```

The final step before jumping in with the Rust programming is writing a `build.rs` file that copies the linker scripts to the correct location for the compiler to find them. I mostly used the example provided in the instructions, but added a section to copy the LiteX file. `export BUILD_DIR` to the location where you generated the LiteX SoC.

```rust
    let mut f = File::create(&dest_path.join("regions.ld"))
        .expect("Could not create file");
    f.write_all(include_bytes!(concat!(env!("BUILD_DIR"), "/software/include/generated/regions.ld")))
        .expect("Could not write file");
```

That's it. Now the code you compile will actually get linked correctly. I found these [iCEBreaker LiteX examples](https://github.com/icebreaker-fpga/icebreaker-litex-examples) very useful to get started. This code will actually run with minimal adjustment on our SoC, and is a good start to get a feel for how the PAC crate works. Another helpful command is to run `cargo doc --open` in the PAC crate to see the generated documentation.

You can now use the runner script to convert the ELF executable to a raw binary and upload it. Don't forget to press the reset button after uploading or nothing will happen.

```bash
cargo run --release
```

From here we "just" need to implement HAL traits on top of the PAC to be able to use almost any embedded library in the Rust ecosystem. However, one challenge is that the peripherals and their names are not exactly set in stone. The way that I solved it is that the HAL crate only exports macros that _generate_ HAL trait implementations. This way your SoC can have 10 SPI cores and you just have to call the `spi` macro to generate a HAL for them. I uploaded the code [in this repo](https://github.com/pepijndevos/rust-litex-hal).

Of course so far we've only used the default SoC defined for the ULX3S. The real proof is if we can add a peripheral, write a HAL layer for it, and then use an existing library with it. I decided to add an SPI peripheral for the OLED screen. First I added the following pin definition

```python
    ("oled_spi", 0,
        Subsignal("clk",  Pins("P4")),
        Subsignal("mosi", Pins("P3")),
        IOStandard("LVCMOS33"),
    ),
    ("oled_ctl", 0,
        Subsignal("dc",   Pins("P1")),
        Subsignal("resn", Pins("P2")),
        Subsignal("csn",  Pins("N2")),
        IOStandard("LVCMOS33"),
    ),
```

and then the peripheral itself

```python
    def add_oled(self):
        pads = self.platform.request("oled_spi")
        pads.miso = Signal()
        self.submodules.oled_spi = SPIMaster(pads, 8, self.sys_clk_freq, 8e6)
        self.oled_spi.add_clk_divider()
        self.add_csr("oled_spi")

        self.submodules.oled_ctl = GPIOOut(self.platform.request("oled_ctl"))
        self.add_csr("oled_ctl")
```

This change has actually been [accepted upstream](https://github.com/litex-hub/litex-boards/pull/96), so now you can just add the `--add-oled` command line option and you get a brand new SoC with an SPI controller for the OLED display. Once the PAC is generated again and the `FullDuplex` trait has been implemented for it, it is simply a matter of adding the [SSD1306](https://github.com/jamwaffles/ssd1306) or [SDD1331](https://github.com/jamwaffles/ssd1331) crate, and copy-pasting some example code. Just as easy as an Arduino, but on your own custom SoC!
