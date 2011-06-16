---
layout: post
title: Blogging like a Ruby Hacker
categories:
- Ruby
- Blogging
- Jekyll
---

Kind of a weird thing to say for someone who does not program Ruby, don't you think?

I have been wanting to move away from Posterous for quite a while now. I've searched for solutions in languages I know and even [spent some time writing something in Clojure][1].

All in all, I wasn't getting anywhere, so I just said to myself that I wanted to have my blog in a static site generator *today*.

I chose [Jekyll][2] because it's the first and most popular one I knew. It's also [used by Github][3] and is named after ["The Strange Case of Dr Jekyll and Mr Hyde"][4]. Only, it's in Ruby.

It turns out Ruby syntax is quite easy to guess right for a Python dev. Before I could get started, I had to hack a [Posterous migrator][5] to support my old links, tags and images.

So, I hope you like my new old theme and that you don't find to much broken stuff.

I don't have comments for the moment, so you'll have to email me when you have problems. I do plan to add search from [Tapir][6] soon.

[1]: https://github.com/pepijndevos/utterson
[2]: http://jekyllrb.com/
[3]: http://pages.github.com/
[4]: http://en.wikipedia.org/wiki/Strange_Case_of_Dr_Jekyll_and_Mr_Hyde
[5]: https://github.com/pepijndevos/jekyll/blob/patch-1/lib/jekyll/migrators/posterous.rb
[6]: http://tapirgo.com/