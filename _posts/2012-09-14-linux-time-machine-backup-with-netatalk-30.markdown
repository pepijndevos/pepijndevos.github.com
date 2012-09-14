---
layout: post
title: Linux Time Machine Backup with netatalk 3.0
categories:
- raspi
- linux
- mac
- timemachine
- netatalk
---

There are quite a few lengthy guides on how to back up your Mac to a Linux box with Time Machine and netatalk 2.x.

You don't need any of that. With netatalk 3.0, it is a single line in your config file, `time machine = yes`, and you don't even need to set `TMShowUnsupportedNetworkVolumes`.

On Arch Linux, all you need to do is

* `pacman -S netatalk`
* Append `dbus avahi-daemon netatalk` to your DAEMONS in `/etc/rc.conf`.
* Add your backup folder to `/etc/afp.conf` like so:

      [ShareName]
      path = /path/to/backup/drive
      time machine = yes

Now start the daemons and you're good to go. Even Bonjour is taken care of automatically.

On Ubuntu, netatalk is still at version 2, so you'll need to [compile it from source](http://netatalk.sourceforge.net/3.0/htmldocs/installation.html#id4561068). Make sure you install Avahi.