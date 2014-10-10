---
layout: post
title: Chromebook Arch Chroot
categories:
- archlinux
- linux
- chromeos
---

I just bought an Asus C300 Chromebook.
They have a really good money/value ratio.
Decent hardware for a fraction of the price of an Ultrabook.

I figured that if it's more powerful than the [Raspberry Pi](/2014/07/30/raspberry-pi-as-my-primary-computer.html),
it's powerful enought to be my work laptop.
The only thing that remains is to put Linux on it.

I read that you can dual-boot or use Crouton to create a Debian chroot.
I think Chrome+terminal is enough for my needs, but I prefer Arch over Debian.

Luckily, it turns out to be rather easy to get an Arch chroot.
These steps only work for Intel Chromebooks.
Basically follow [these steps on the wiki](https://wiki.archlinux.org/index.php/Install_from_Existing_Linux).

First thing to do is to get in developer mode, otherwise you can't access the shell.
Press `esc+refresh+power` and follow the steps on the screen.
This will take a few minutes and wipe out your drive.

Next open Crosh with `ctrl+alt+t` and open a full Bash console with `shell`.

Enabling developer mode disables OS verification,
which is nice if you want to run your own OS.
However, if you just want a chroot, it's safer to turn verification back on.

    sudo crossystem dev_boot_signed_only=1

Next download the Arch bootstrap archive from any mirror.
Not the ISO, mind you.
Chrome OS doesn't understand gzip to my surprise, but this is not an issue.

I had some issues with the cursor getting stuck.
This can be fixed by installing [Secure Shell](https://chrome.google.com/webstore/detail/secure-shell/pnhechapfaindjhompbnflcldabbghjo)

Now we need to extract the bootstrap archive to a proper location.
Most of the partitions are mounted with the `ro` and `noexec` flags,
which foiled my initial attempt at entering the chroot.
However, `/usr/local` allows writing and execution.

    cd /usr/local
    tar xf /home/user/blablabla/Downloads/archlinux-bootstrap-yyyy.mm.dd-x86_64.tar.gz
    sudo vim root.x86_64/etc/pacman.d/mirrorlist
    sudo root.x86_64/bin/arch-chroot /usr/local/root.x86_64/
    pacman-key --init
    pacman-key --populate archlinux
    pacman -Syu

And that's all there is to it. You can now install anything you want inside the chroot.
I went ahead and installed vim and git.
I'm now writing this post in my Arch chroot.
