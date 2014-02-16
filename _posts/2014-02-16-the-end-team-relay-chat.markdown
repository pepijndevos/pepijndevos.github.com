---
layout: post
title: The end of Team Relay Chat
categories:
- irc
- teamrelaychat
---

It's time to end this experiment. It's as easy as disabling the sign-up button, as there are no users to notify.

### How it started

During my batch at Hacker School we used IRC to communicate. I still have the channel open in my [IRC bouncer](http://teamrelaychat.nl/bouncer/), but after my batch they started using some web application which I never really used.

At about the same time I worked for a company where they used Campfire internally, which I also never really used or liked.

So the idea was born to build a collaboration platform based on IRC. I started hacking and it worked. The rest was just an excercise in finishing and shipping.

### Users!

After I posted my first working version to Hacker News I had maybe 5 trial users and a lot of people chatting on the demo server. I got some useful feedback.

This was all very exciting, but it didn't last. I dropped of the front page, trial periods ended and things became quite.
I'd have maybe a signup every week, but noone stayed.

I tried Google AdWords, but that stuff is *hard*.

### Doing things that don't scale

The sign-up process worked like this:

1. An email arrived.
2. I sent a reply to verify they where not bots.
3. I rented a VPS.
4. I ran my deployment script.
5. I sent another email telling the user their server was ready.

This was actually very funny, because I could tailor the email to the user. They still thought I was a machine most of the time, but I also had some nice exchanges.

Some people also submitted multiple times because they did not get a reply within 5 minutes. After I changed "Sign up" to "Request invite" this went better.

### Technical problems

The system I made worked pretty well, but deployment could have been better.

I used a Pallet script that would break every other deploy. The authors where *very* helpful in fixing all the problems, but I would nevertheless pick another system next time I need to automate deployments.

Every user had its own VPS. This was before Docker, so multi-tenanting and isolating IRC servers was hard. IRC doesn't have virtual hosts you know. In practice this meant signing up new users was slow and expensive.

### Bigger problems

The only real problem with this project is that I can't sell it. I can't even explain to you why this is better than email or Facebook.

It basically comes down to "I like IRC". Other than that, Hipchat has more features and better UX.

The project was born as a hack and something I would use. I had and still have no idea why anyone else would use it.

There are developers who don't use IRC, and non-developers that don't even know what IRC is. Who would have thought...

### Conclusion

It was fun, I learned. Next time I build somthing, I should figure out if and why people want it.

[TRC is on Github](https://github.com/pepijndevos/irc-deploy), so if you do care, you can run your own server. The deploy script is probably broken though.

There is one instance of TRC still running as a bouncer that actually has users, including myself. If you are looking for a bouncer with a fancy web interface, [you can have it](http://teamrelaychat.nl/bouncer/) for 3 Euro per month.

It's all very small-scale, so if you want some random plugin installed, you can probably have it. Chances are I won't renew the wildcard SSL cert though, these things are expensive.
