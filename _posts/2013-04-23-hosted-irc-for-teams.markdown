---
layout: post
title: Hosted IRC for Teams
categories:
- irc
- teamrelaychat
---

It has started. A few weeks ago I was just playing around with [Pallet](http://palletops.com/), now I have been on [Hacker News](https://news.ycombinator.com/item?id=5584817) and have a couple of users.

My workflow is pretty desktop oriented, so when I was required to use a web application to keep in touch with teammates and Hacker School friends, I thought I could make a perfectly fine group communication tool based on IRC with plenty of desktop clients available.

The idea is pretty simple: Give every team a VPS with an IRC server, but the devil is in the details. A vanilla IRC server is a bit awkward as a collaboration tool. You might as well telnet to each other directly.

An important piece to the puzzle is ZNC. ZNC is a powerful bouncer with a ton of plugins. It stays connected to the server on your behalf 24/7, replaying all the messages you missed while disconnected. So that is user accounts and persistent chat right there.

Another important part is Hubot. Hubot is an IRC bot that can execute commands and notify you about things like Git commits, build failures and more.

There is also a web interface, because… you know, some people do live on the web.

Finally, I have some grand plans for file sharing, but that is for another time.

The result speaks for itself, check it out: [teamrelaychat.nl](http://teamrelaychat.nl/)

I realise this is not for everyone, but if you're anything like me, chances are it fits your workflow better than these web based services.