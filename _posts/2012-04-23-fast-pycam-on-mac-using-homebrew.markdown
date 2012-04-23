---
layout: post
title: Fast PyCAM on Mac using Homebrew
categories:
- python
- mill
- gcode
- mac
---

Ever since I started working on my CNC mill, I have been looking for decent software to generate G-code for it.

I [wrote a G-code interpreter][1] for my machine, after I found out LinuxCNC doesn't support my type of LPT stepper driver, but finding good software to turn a 3D model into G-code turned out to be hard.

I went down [this list][2], but most of them where hard to install or impossible to configure. In 2D I had some luck with the Inkscape plugin, but I was lost for 3D.

With the mill in a working state, and nothing to mill, I decided to push forward with PyCAM. I skipped PyCAM previously, because of its [dependencies][3], which weren't all available in Homebrew.

Some experimentation revealed a few important things:

* It runs okay on my Ubuntu netbook.
* Toolpath calculations take a looong time.
* Its CLI runs without OpenGL and GTK+
* It supports multiple and distributed processors.

After I cancelled the Ubuntu calculations, I ran it headless on my Mac. While somewhat faster, it still took a long time to generate G-code.

The real speedup came later, when I found PyCAM supports Psyco. But rather than messing with Psyco, I followed the advice on the Psyco homepage, and found the headless PyCAM runs great, and very fast, on PyPy.

> Psyco is unmaintained and dead. Please look at PyPy for the state-of-the-art in JIT compilers for Python.

After successfully running PyCAM headless, I finally put my teeth in the dependencies. Installation steps using Homebrew follow:

1. [Download PyCAM][4].
2. Apply [my patch][5] to make sure it finds pygtk.
3. `brew install pygtk`
4. `sudo easy_install PyOpenGL`
5. Wait for my [pygtkglext Formula][6] to be merged, or get it from my branch.
6. `brew install pygtkglext` or `brew install https://raw.github.com/pepijndevos/homebrew/master/Library/Formula/pygtkglext.rb`

Now you should be able to just run `./pycam` to see the GUI pop up. However, I use the following 2 commands to run a server on PyPy, for extra speed.

    pypy pycam --start-server-only --server-auth-key=fietspomp --number-of-processes=4

    python pycam --enable-server --remote-server=localhost --server-auth-key=fietspomp --number-of-processes=0

I'll blog about the mill itself later. This morning I tried to mill a small sample project, but the mill is very inaccurate and jammed after a few layers.

[1]: https://github.com/pepijndevos/Home-made-CNC
[2]: http://replicat.org/generators
[3]: https://sourceforge.net/apps/mediawiki/pycam/index.php?title=Requirements
[4]: http://pycam.sourceforge.net/download.html
[5]: https://sourceforge.net/tracker/?func=detail&aid=3520280&group_id=237831&atid=1104176#filebar
[6]: https://github.com/mxcl/homebrew/pull/11801