---
layout: post
title: Raspberry Pi as my primary computer
categories:
- raspberrypi
- linux
---

![my work](/images/raspi/tmux.png)

It occurred to me that most of the work I do on my shiny dual core i7 ultrabook is using VIM, Python, ssh, and other command-line tools. So why do I need a fast CPU and fancy graphics?

When the Raspberry Pi B+ was released, I knew I had to try this. I ordered one, installed Raspbian and all the stuff I needed, and took it to work.

![my desk](/images/raspi/desk.jpg)

I hooked up an external monitor, keyboard, and mouse. Then I sneaked a LAN cable into the WiFi hotspot and set to work.

I decided to forgo X entirely. As I mentioned, I use mostly terminal applications, and alternatives for the few GUI apps I use are readily available. 

After a week, I can say it worked surprisingly well. I could pretty much do my job as usual. Most applications never used much CPU or memory. And even while compiling Python 3.4 from source, my terminal stayed snappy.

The only mayor problem is browsing the web. Elinks is pretty good, and most things that do not use JS are usable, but it’s not ideal. At times I had to `startx` and fire up Midori. It’s so much slower and resource hungry than elinks though.

On the upside, RFC’s look pretty much unchanged. Still dry and boring.

The one application that makes this work is tmux. Without tmux there is no way this would ever work. Tmux is my tiling window manager. Everything I do, I do it in tmux.

The best part is that you can `startx`, open a terminal, run `tmux attach` and continue working alongside the sluggish GUI apps. You could even walk over to a colleague, ssh to your machine and work there.

I use fbterm as my terminal emulator. This allows you to use your favourite monospaced font in a framebuffer. I use Zsh for my shell. I can highly recommend the `.zshrc` from [grml](https://grml.org/zsh/).

I recommend that you enable overclocking and adjust the memory split to 16MB to give you all the resources you can get. However, this is mostly for web browsing, on the console you don’t notice the difference.

During the weekend before the experiment, I had a lot of fun setting up all the tools I need and more.

I set up alpine for email and weechat for IRC, but also youtube-dl and mplayer for playing videos in a framebuffer.

I did experiment with netsurf. A graphical browser in a framebuffer. The problem is that there is still no JS, and there is no way to quit it. So for times that elinks is not enough, you really want proper webkit.

In the case you want to try this, here are some essential keyboard shortcuts I learned in te past few days.

Tmux commands are all prefixed with `ctrl+b`. Put `set -g mode-keys vi` in `~/.tmux.conf` for copying to work.

    c   new pane
    p   previous pane
    n   next pane
    0-9 go to pane
    %   split vertically
    o   other split pane
    [   start selection/scroll mode
            space start selection
            enter copy selection
            v     toggle block selection
            q     exit mode
    ]   paste selection

VIM commands for working with tabs

    :tabedit {file} open file in new tab
    gt              go to next tab
    gT              go to previous tab
    {i}gt           go to tab in position i

Elinks also has tabs. I created a little alias to launch google results from the terminal directly.

    t create tab
    c close tab
    > next tab
    < previous tab

Weechat is mostly controlled by IRC commands I useh anyway such as `/join` and `/quit`. To switch buffers you press `alt+0-9`. It really helps to install the `buffers.pl` script so you can see a numbered list of them.

If you `startx` use, `ctrl+alt+F1-F7` to change TTY.