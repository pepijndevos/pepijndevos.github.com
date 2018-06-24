---
layout: post
title: "sysenv: virtualenv for system packages"
categories:
- linux
---

<script src="https://asciinema.org/a/ef0ocqBV196dz2EcddncL1Pz9.js" id="asciicast-ef0ocqBV196dz2EcddncL1Pz9" async> </script>

I try to keep my Linux system as clean as possible, but for almost every project I inadvertently have to install some packages with `apt-get`, build some from source with `make install`, or even install some proprietary program that in turn requires more packages. So over time my system acquires more and more junk.

To solve this problem, I made "`virualenv` for system packages", a little script that makes a `chroot` with an unholy combination of OverlayFS and bind mounts. From inside the `chroot`, it looks and behaves exactly like your `/`, with no overhead or isolation whatsoever. The only difference is that writes to all system directories go to an overlay directory.

	$ sudo bash activate.sh
	(env)$ sudo apt-get install everything
	(env)$ sudo make install
	(env)$ echo "Hello world" > hello.txt
	(env)$ ^D
	$ everything
	Command not found
	$ cat hello.txt
	Hello world

In the Python world this is pretty much the standard. You make a `virtualenv`, `pip install` all the things you need, and delete then environment after your project is done. I want this kind of behavior for *all* of my software.

Of course you can run everything in a VM, a Docker image, or a `chroot`. But these typically provide isolation that I don't want or need. They also have a lot of overhead in RAM, disk space, and most importantly, effort on my part. However, the git-like overlay filesystems used by Docker gave me an idea.

What if I made a `chroot`, but instead of putting an entire Debian installation inside it, make an overlay on my own system. The only trouble is that special directories like `/proc`, `/sys`, and `/dev` should work as usual, and preferably my `/home` folder should also persist outside the `chroot`.

The solution turns out to be relatively simple: My `/home` and all the special directories are mounted with `mount --bind`. All system directories like `/lib` and `/bin` are mounted with `mount -t overlay`.

It works great for from-source installs and proprietary software. It also works for `apt-get`, but I'm sure weird things will happen once you upgrade your system. Once it gets wonky, just nuke the env and start over.

[Get the code here](https://github.com/pepijndevos/sysenv), pull requests welcome.
