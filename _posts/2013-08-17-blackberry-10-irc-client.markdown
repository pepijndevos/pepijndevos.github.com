---
layout: post
title: BlackBerry 10 IRC Client
categories:
- app
- blackberry
- irc
---

![screenshot of TinCan](/images/tincan.png)

While working on TRC I made a collection of [popular IRC clients per platform](http://teamrelaychat.nl/wiki/irc_clients), only to realise BlackBerry 10 had no real IRC clients. The best one available seemed to be an Android client running in an emulator.

So I asked a friend, collector of phones and owner of [Heris IT](http://www.heris.nl/) to help me build a native IRC client for BlackBerry 10 using their Qt based Cascades framework.

A few months later, [the first usable version is available for sale on the BlackBerry World](http://appworld.blackberry.com/webstore/content/33994898/).

This is a beta release that costs half of the final product, but also has half the features. We are doing this early release to collect some feedback, so please [let us know](https://groups.google.com/forum/#!forum/tincan-irc) what is important to you.

There are a few known issues:

* Sending messages is broken on 10.2
* The "Settings" button is a dummy.
* The "Change nick" button is a dummy.
* When the phone is standby the server kicks you of after a few minutes.

Some planned features:

* Richer message display
* Smarter notifications
* Tighter ZNC integration
* Nick autocomplete
* Remember channels
* SASL support

And finally a recommendation:

* [Get a bouncer](http://teamrelaychat.nl/bouncer/), it makes everything better