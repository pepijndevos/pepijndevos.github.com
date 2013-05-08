---
layout: post
title: "Team Relay Chat: File Sharing and Registration"
categories:
- teamrelaychat
- IRC
- registration
- filesharing
---

Almost a month ago I uploaded the first version of Team Relay Chat, now I have a couple of users and some nice new features to show you.

### Registration

Previously it required several not-so-trivial steps to add a user to your IRC server. Now you can just enable the registration module. Your employees will be able to just pick a user name and password and be good to go.

![registration page](http://teamrelaychat.nl/wiki/_media/screen_shot_2013-04-30_at_11.11.05_am.png)

### File Sharing

I realised file sharing was important right from the beginning.
It has been possible to share files from the web interface for quite some time now, but I added two important features.

You can now share files from your desktop client too. Just send them to Hubot via DCC, and Hubot will take care of uploading them.

<iframe width="560" height="315" src="http://www.youtube.com/embed/DgTKyHY-3pQ" frameborder="0" allowfullscreen="allowfullscreen"> </iframe>

The web client now displays a preview of uploaded images inline. No more no less.

### Documentation

I'm working hard to make TRC as easy to use as possible. Both by just making things simpler and by writing documentation and even doing some videos.

All of this can be found at [teamrelaychat.nl/wiki](http://teamrelaychat.nl/wiki/)

### Log Viewer

ZNC comes with a lovely logging module that stores log files of all your chats in a folder deep inside the server.

It is now possible to browse all these files via the ZNC web interface. This interface will grow even better in the next few weeks.

### Firewall friendly

Nowadays almost everyone is behind a NAT or firewall, and I've been working to make sure you can use Team Relay Chat even then.

#### Alternative IRC ports
 
You can now configure ZNC to listen on any port you want, even lower port numbers like 25(SMPT) or 80(HTTP) are supported. So even if port 6697 is blocked, you can still chat.

#### Passive DCC SEND

File sharing from the desktop uses DCC. Normally this means the sender(you) opens a port from which the receiver(hubot) downloads the file. This means you have to forward external ports to your machine.

However, Hubot also support passive DCC. In this case Hubot opens a port and you simply upload the file. No configuration needed.

### Conclusion

You should try the [demo server](irc://irc.teamrelaychat.nl:6697) and [read more](http://teamrelaychat.nl/).