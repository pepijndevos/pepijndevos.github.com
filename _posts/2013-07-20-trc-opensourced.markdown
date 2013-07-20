---
layout: post
title: TRC Open-sourced
categories:
- clojure
- pallet
- irc
- teamrelaychat
---

[pepijndevos/irc-deploy](https://github.com/pepijndevos/irc-deploy)

I finally took some time to cut out the private bits from my deploy script and open source it to the world.

This script allows you to deploy an IRC server, bouncer, web interface, bot and some [custom modules](https://github.com/pepijndevos/irc-utils) to anything you can think of: VirtualBox, VPS, EC2, OpenStack, Docker…

The only things that are missing are the SSL certificates and services.

You can add your own certificates, generate self-signed ones, or disable SSL altogether:

    znc --makepem
    openssl req -new -newkey rsa:4096 -days 365 -nodes -x509 -subj \"/C=NL/ST=Gelderland/L=Loenen/O=Wishful Coding/CN=*.teamrelaychat.nl\" -keyout node.key  -out node.cert

Your services are entirely up to you, but I personally use a `vbox` service for testing:

    {:vbox {:provider "vmfest"}}

Now you should be able to just run

    lein pallet up --service vbox --roles dev

Of course you can also just [let me deploy it for you](http://teamrelaychat.nl) or hire me to deploy other things.