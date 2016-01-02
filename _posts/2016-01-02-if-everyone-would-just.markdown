---
layout: post
title: If Everyone Would Just...
categories:
- rust
- c
---

While everyone was making new years resolutions for themselves, I was making new years resolutions for the world. Or rather, thinking how change happens.

It seems people are hardly able to change themselves, even if they set out to do so. So how can we even begin to think about changing humanity as a whole? It seems we just keep doing whatever is easiest/best/most comfortable, and change only happens when something easier/better/more comfortable comes around.

Sometimes you read about [these civilizations](https://en.wikipedia.org/wiki/Societal_collapse) that seemed to be really advanced for their time, but then just sort of died out. Looking back it is often obvious why, and you wonder if they did not see it coming. But what if they saw it coming all along, but were unable to change their ways?

The only mechanism we have for global change is governance. But as Douglas Adams explains

> One of the many major problems with governing people is that of whom you get to do it; or rather of who manages to get people to let them do it to them: It is a well known fact, that those people who most want to rule people are, ipso facto, those least suited to do it. Anyone who is capable of getting themselves into a position of power should on no account be allowed to do the job.

Often times I have an idea to make something somewhere a little better. No grand plans to solve world hunger, just small things. Often by means of sharing or exchanging information or goods. Without exception, these ideas include the sentence "If everyone would just..." and you quickly learn about the [Network Effect](https://en.wikipedia.org/wiki/Network_effect).

I'm just back from 32c3, where I saw a number of interesting art projects.
Many of these projects used technology not to provide utility, but to convey a message.
Thinking about all the above, I built [The World Improvement Server](https://github.com/pepijndevos/world_improvement_server); a caricature of global change.

Th World Improvement Server runs a program that speaks the SUN-RPC rwall protocol, listening for your world improvement ideas. These ideas are then broadcast over the QOTD protocol to anyone who cares to listen.

To start improving the world, simply run `rwall 37.247.53.27` and type your idea, followed by EOF.

To learn more about ways to improve the world, simply `telnet 37.247.53.27 17`, which as of 2 January 2016 returns

	$ telnet 37.247.53.27 17
	Trying 37.247.53.27...
	Connected to 37.247.53.27.
	Escape character is '^]'.

	If everyone would just [...]
	the world would be a much better place!

	Submit global world improvement using rwall.

	Remote Broadcast Message from pepijn@pepijn-Latitude-E6420
		(/dev/pts/0) at 18:19 ...

	If everyone would just use this service to make the world a better place
	the world would be a better place.


	Connection closed by foreign host.
