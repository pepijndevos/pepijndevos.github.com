---
layout: post
title: VMfest Base Image
categories:
- clojure
- vmfest
---

I'm playing with Pallet to setup an IRC server and bouncer. Pallet uses vmfest to deploy to VirtualBox.

While vmfest comes with some prepared images, I found that my laptop would not run them, so I made my own, with *a lot* of help from Antoni Batchelli.

* Create an image in VirtualBox with a NAT and host-only network.
* Install your favourite distro.
* Install an SSH server.
* Setup [passwordless sudo](http://serverfault.com/a/160587)
* Make sure /etc/network/interfaces contains both eth0 and eth1.

        auto eth0
        iface eth0 inet dhcp
        auto eth1
        iface eth1 inet dhcp

* Update and upgrade all packages.
* On some Debian based distros, [remove persistent-net.rules](http://www.ducea.com/2008/09/01/remove-debian-udev-persistent-net-rules/).

This should give you a working machine, but we're not done yet. You still need to make the hard disk [multi-attachable](http://www.virtualbox.org/manual/ch05.html#hdimagewrites).

If you where to use this image in Pallet, you could only use it once. Multiattach means that every time a machine is made, a new copy-on-write image is created so the original stays intact.

To do this, delete the VM, *but not the vdi file*, and run the following command:

    VBoxManage modifyhd the/disk.vdi --type multiattach

Finally, you need to create a meta file with the same name as the disk image, but with the .meta extension. As an example:

     {:os-type-id "Ubuntu_64",
      :sudo-password "vmfest",
      :no-sudo false,
      :username "vmfest",
      :os-family :ubuntu,
      :os-version "12.04",
      :os-64-bit true,
      :password "vmfest",
      :description "Ubuntu 12.04 (64bit)"
      :packager :apt}