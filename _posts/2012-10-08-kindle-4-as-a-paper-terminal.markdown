---
layout: post
title: Kindle 4 as a paper terminal
categories:
- kindle
- java
- epaper
- terminal
---

After reading about the [Kindleberry Pi][0], I really wanted to experiment with e-paper as a computer monitor.

Sadly, I own a Kindle 4, which has no keyboard and consequently  a lot less hacks.  Still, I'm typing this post from my Kindle, so I will document how I got here.

Initially, I could not find a jailbreak for the Kindle 4, but at some point, I found [this wiki page][1] that documents everything you need to get started.

I used the universal method, which involves downloading `data.tar.gz`, entering and leaving diagnostic mode and rebooting a few times.

The jailbreak only installs a developer key, that allows you to install software. The piece of software we need is usbnetwork, which allows you to SSH over USB.

[Download usbnetowrk from here][2] and follow the instructions.

Before you enable usbnetwork by renaming the `auto` file, go into `/usbnet/etc/config` and set `USE_VOLUMD="true"`

If all went well, the next time you connect your Kindle, it will present a network instead of mass storage.

You should now be able to `ssh root@192.168.2.2`. Linux users should run `sudo ifconfig usb0 192.168.2.1 netmask 255.255.255.0` first, to bring up the network.

The terminal emulator used in the Kindleberry hack does not work on the Kindle 4. It expects a keyboard, I've been told.

However, vdp wrote a Java kindlet terminal emulator called KindleTERM that seems to run on Kindle 4.

KindleTERM was writen 2 years ago, and while it pretty much worked out of the box, it did not make an SSH connection to my PC.

I finally got something working using the remote keyboard feature, sshing to localhost and using `dbclient` to ssh into my Mac and start a `tmux` session.

Hawhill made a new version that just telnets to localhost, but he removed the remote keyboard. That's useless.

Then hippy dave came around and added back the remote keyboard. Yay!

Then I came around and added a config file to specify which host/port/username/password/command to use.

Long story short, follow [his instructions][4], but upload [my version][3] of KindleTerm to your Kindle. You should be using SCP or SFTP, since you can no longer use mass storage.

By default, my version still telnets to localhost, but it tries to read from `/developer/KindleTermPV/work/kindleterm.properties`. Mine looks like this:

    host=192.168.2.1
    login=pepijn
    password=secret
    cmd=tmux -S /tmp/kindle

After rebooting, you should see KindleTermPV on your home screen. When you start it, it should telnet to your PC.

If you see a blank screen, press back+keyboard or relaunch the app a few times.

If you are on Mac, you can run `telnetd` with `sudo launchctl load -w /System/Library/LaunchDaemons/telnet.plist`

You might want to `export PS1=">"` to save some precious screen space.

You should also `export TERM=ansi` if you experience any formatting problems. The VT320 implementation is a bit buggy.

![kindleterm screenshot](/images/screen_shot-40716.gif)

[0]: http://projectdp.wordpress.com/2012/09/24/pi-k3w-kindle-3-display-for-raspberry-pi/
[1]: http://wiki.mobileread.com/wiki/Kindle4NTHacking
[2]: http://www.mobileread.com/forums/showthread.php?t=88004
[4]: http://www.mobileread.com/forums/showthread.php?t=107192
[3]: https://github.com/pepijndevos/KindleTerm/downloads
