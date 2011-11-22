---
layout: post
title: Twisted on Dotcloud
categories:
- twisted
- dotcloud
- python
- twitter
---

A while back, I wrote a [Twisted email server](http://pepijndevos.nl/Twemail/) that would sit between you and Twitter and let you reed and send tweets via email.

With the advent of free cloud hosting, I thought it'd be fun to put it online. I'll share with you how it's done, for those of you who are looking for a place to put your Twisted app.

The major problem with these easy hosts is that they assume a simple WSGI app or a background worker, but then the background worker is not accessible from the outside.

First I tried Heroku, then Dotcloud, and finally a couple of others, and just when I had given up, @solomonstre came in:

> ＠pepijndevos dotcloud has beta support for arbitrary tcp/udp ports. Want to try? :)

After some fruitless tries, he shared [this repo](https://github.com/jpetazzo/python-worker-on-dotcloud), which contains scaffolding for a web-accessible Python worker.

The core parts of this thing are dotcloud.yml, where you define a setup script, the ports you want, and the command to run your app.

    worker:
      type: custom
      buildscript: builder
      ports:
        smpt: tcp
        pop3: tcp
      process: ~/run
      approot: twemail
      environment:
        PYTHON_VERSION: 2.7

`builder` contains a whole lot of pip/virtualenv code, while `run` contains something like `twistd -ny yourapp.tac`.