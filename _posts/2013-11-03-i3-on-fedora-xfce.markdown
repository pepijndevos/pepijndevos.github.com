---
layout: post
title: i3 on Fedora Xfce
categories:
- linux
- i3
- xfce
- fedora
---

![i3 on xfce](/images/i3-xfce.png)

It's distro hopping time again. This time I decided to Pick Fedora with Xfce as the desktop environment and i3 as the window manager.

I previously used Crunchbang and Arch before that. On Arch I built my own setup from scratch, but kept running into missing features and breakage. On Crunchbang I used the default setup, but being a Debian derivative, it has very old software.

What I hope to find in Fedora's Xfce spin is a light distro that is up-to-date and provides a nice out-of-the-box experience. I only want to replace xfwm4 with i3.

Most of the stuff in this post is based on [this guide](http://forum.manjaro.org/index.php?topic=6831.0) adapted for Fedora and my personal preferences.

The first step is easy:

    yum install i3

Next, you need to stop the Xfce window manager and start i3 instead.

In the Xfce menu, go to "Session and Startup" and then to "Application Autostart". Add an entry for i3 there.

Stopping `xfwm4` is done in the "Session" tab. Simply set its "Restart Style" to "Never".

You might also want to stop `xfdesktop` and `xfce4-panel`. `xfdesktop` provides a window with icons, which is awkward when it gets tiled by i3. `xfce4-panel` provides the menu bar, which I kept around until I was sure I could do everything I needed using i3.

Then I made some modifications to the i3 config file.

    # use the Xfce terminal instead of urxvt
    bindsym $mod+Return exec xfce4-terminal
    # mimic Crunchbang, I remapped tabbed layout to $mod+t
    bindsym $mod+w exec firefox
    # make sure xfce notifications display properly
    for_window [class="Xfce4-notifyd"] floating enable;border none; focus mode_toggle
    # uncomment the i3 dmenu wrapper
    # this gives you easy access to Xfce settings
    # and hides obscure utilities
    bindsym $mod+d exec --no-startup-id i3-dmenu-desktop
    # remap the exit command to properly exit Xfce
    bindsym $mod+Shift+e exec xfce4-session-logout
    # use conky for displaying some stats
    bar {
        status_command conky
    }

My `.conkyrc` is very simple

    out_to_console yes
    out_to_x no
    TEXT
    RAM ${membar} | \
    CPU ${cpubar} | \
    BAT ${battery_bar BAT1} | \
    $acpitemp C | \
    ${time %a %d %b %R}

The only thing that remains is installing patented software for proper font rendering and playing multimedia. Fedora is made by RedHat which is based in the US. It turns out you can't have subpixel smoothing or play MP3 files because of that.

If you're wondering why this is not a problem for Arch or Ubuntu, it is because Canonical is based on the Isle of Man and Arch isn't based anywhere at all.

The solution to all of this is to take a look at [RPM Fusion](http://rpmfusion.org). A third-party repo which has all the stuff you need.

Alternatively there is also a repo for the [Freetype infinality patches](http://www.infinality.net/blog/infinality-repository/), but so far I have not managed to make my fonts look the way I like.