---
layout: post
title: Warning for changes in Arch
categories:
- archlinux
- cron
- rss
- python
---

I'm using Arch on my new laptop because of problems with Fedora. But one problem with Arch linux is that it sometimes requires manual intervention when updating. If you don't pay attention, this command might break your system.

    pacman -Syu

The solution of course is to diligently check [archlinux.org/news](https://www.archlinux.org/news/) for breaking changes. But I obviously forget that all the time. If I have time at all.

Thinking this over, I was reminded of an annoying feature in OS X where if a cron job produced output, it would put that output in an mbox file. As long as you don't clean up that file, every new terminal will say "You have new mail." at the top.

Wouldn't it be great if every new terminal would warn you about upgrading? Wish no more. I did exactly that with a few small scripts.

The first is a script that downloads the Arch RSS feed to a mbox, added to my crontab.

    sudo systemctl enable cronie
    crontab -e
    0 * * * * /home/pepijn/bin/archheadline.py

{% highlight python %}
{% include code/archnews/archheadline.py %}
{% endhighlight %}

The second is a script that should be added to your `.zshrc` or `.bashrc` depending on your shell. It will check the mbox file for new messages and print a notice to your terminal.

    ~/bin/unread.py || echo "$? breaking changes."

{% highlight python %}
{% include code/archnews/unread.py %}
{% endhighlight %}

To get rid of the warning, open the mbox with a mail viewer. Probably mutt, knowing a few Arch users.
I simply run `mail` to mark the messages as old, and optionally actually read them.

    mail -f /var/spool/mail/news
    p 1 # print first message
    q   # write mbox and quit
