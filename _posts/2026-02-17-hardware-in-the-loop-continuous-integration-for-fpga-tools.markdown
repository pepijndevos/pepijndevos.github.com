---
layout: post
title: Hardware in the Loop Continuous Integration for FPGA tools
categories:
  - fpga
---

Imagine you are the maintainer of an open source FPGA toolchain such as [Apicula](https://github.com/YosysHQ/apicula) and you want to make sure your code works. The industry standard for making sure software works is to set up continuous integration that tests your code for every change you make.

But what do you test? We are reverse engineering the inner workings of complicated devices, and if you're slightly wrong you can generate a good looking bitstream that just doesn't work. So of course during development you test your bitstreams on real hardware to make sure they work. And on top of that we _do_ set up CI, but it can only test that we can generate a bitstream.

This is not idle thought, just [yesterday](https://github.com/YosysHQ/apicula/pull/462) we had one FPGA silently fail due to improvements in another. It is very hard to catch these regressions reliably without constantly manually testing every single devices, which is completely infeasible.

For a long time I've dreamed of setting up a raspberry pi with a bunch of FPGAs and somehow running automated tests on it. But it always felt like a huge ordeal to really put everything together. Running and maintaining a raspberry pi with a bunch of FPGAs, writing useful self-tests that automatically verify correct behaviour, triggering a test run, downloading or generating bitstreams, programming the FPGAs, obtaining and verifying the results, and communicating that back to Github.

But this week I looked at the problem again and realized all the pieces had falling into place to make this not only feasible but almost trivial.

1. I am already running a Raspberry Pi with Home Assistant on my very own Mini-ITX motherboard: [Sentinel Core](https://www.crowdsupply.com/sanctuary-systems/sentinel-core)
2. For running LLMs on my pi, I built [custom Docker addons](https://github.com/sanctuary-systems-com/llm-addons) for Home Assistant
3. To build those Docker containers, I'm using a Github [self-hosted runner](https://docs.github.com/en/actions/concepts/runners/self-hosted-runners) on my VPS
4. We have [femto RISC-V UART](https://github.com/YosysHQ/apicula/blob/master/examples/femto-riscv-18.v) examples now based on Bruno Levy's [FPGA tutorials](https://github.com/BrunoLevy/learn-fpga)

So the plan is simple, plug some FPGAs into Home Assistant, run a self-hosted runner as an addon, and add a CI task that uploads an example and verifies its UART output against a reference.

For the addon I just forked an existing addon and added USB access and openFPGALoader: [home-assistant-github-runner-add-on](https://github.com/pepijndevos/home-assistant-github-runner-add-on)

For the CI side, it basically just adds a sel-hosted step after our current bitstream generation that fetches the bitstream artifacts, uploads them, and diffs the UART output: [Add hardware-in-the-loop CI test infrastructure](https://github.com/YosysHQ/apicula/pull/461)

I'm excited to bring this new level of reliability to Apicula, and curious if other projects could do something similar.
