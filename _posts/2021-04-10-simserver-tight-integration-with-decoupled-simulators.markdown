---
layout: post
title: "SimServer: tight integration with decoupled simulators"
image: /images/simserver/ringing.png
categories:
-mosaic
-spice
---

I am working on Mosaic, a modern, open source schematic entry and simulation program for IC design. With a strong focus on simulation, I want to offer deep integration with the simulator, but also be able to run it on a beefy server and shield my program from simulator crashes. To this end, I have developed an RPC abstraction for interfacing with simulators remotely.

Here is a demo of a [short Python script](https://github.com/NyanCAD/SimServer/blob/main/examples/blink_multisim.py) that uses Pandas, Matplotlib, and Cap'n Proto to run a CMOS netlist on Ngspice and Xyce and a behavioural Verilog equivalent on CXXRTL, allowing for easy verification of the transistor implementation.

<iframe width="560" height="315" src="https://www.youtube.com/embed/cgA93NP9lU4" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen> </iframe>

You can see that the behavioural simulation is nearly instantaneously, while the spice results stream in much slower because they are doing a fully analog transistor simulation. You can see there is a bit of overshoot at the edges, and zooming in on that, you can see minor differences between the analog simulators because Xyce is using an adaptive timestep. 

![close up of overshoot](/images/simserver/ringing.png)

Now let's take a step back and take a look at the design and implementation of this system. There are several reasons why I chose for a simulation server.

* Ease of installation. Xyce is notoriously hard to install and only works on Linux as far as I know. An RPC protocol allows Xyce to run in a Docker container.
* Performance. My laptop might not be the best place to run the simulation. An RPC protol allows the simulator to run on a beefy server, while running my user interface locally for a snappy experience.
* Integration. Running a simulator in batch mode provides no indication of progress and requires setting up and parsing output files. An RPC protocol allows for direct, streaming access to simulation results.
* Stability. It would not be the first time I've seen Ngspice segfault, and I'd hate for it to take the user interface along with it. An RPC protocol allows the same tight integration as its C API without linking the simulator into the GUI.

For the RPC library I settled on Cap'n Proto, but the next question is, what does the actual API look like? Ngspice has quite an extensive API, but the same can't be said for Xyce and CXXRTL. So I could offer the lowest common denominator API of "load files, run, read results", but one of my main goals was deep integration, so this is unsatisfactory. What I ended up doing is define small interfaces that expose a single functionality, and use multiple inheritance to assemble simulator implementations.

So I currently have 3 implementations of the `run` interface, and on top of that Ngspice implements the `tran`, `op`, and `ac` interfaces, with more to follow. I hope that in the future [JuliaSpice](https://juliacomputing.com/media/2021/03/darpa-ditto/) will be a simulator that provides even deeper integration.

Please check out the code, and let me know your thoughts: https://github.com/NyanCAD/SimServer (How to expose simulator configuration and other functionality? Can we do remote cosimulation? Any other interesting usecases?)

Meanwhile, here is a demo of the example Python client running a transient and AC simulation on my VPS.

```bash
# on my VPS
docker pull pepijndevos/ngspicesimserver:latest
sudo docker run -d -p 5923:5923 pepijndevos/ngspicesimserver:latest
```

```bash
# in the examples folder
python ../client.py ngspice myvps:5923 rc.sp tran 1e-6 2e-3 0 ac 10 1 1e5
```

![transient result](/images/simserver/tran.png)
![AC result](/images/simserver/ac.png)

