---
layout: post
title: NIO in Clojure
categories:
- clojure
- nio
---

So, I'm playing with the idea of writing a little webserver in Clojure, much like those asynchronous Python ones like Twisted and Tornado, but actually I'm just writing this to play with my new blog.

There you have the code, not as a gist this time. The code actually [lives in a separate file on Github][1], and is included here by a few lines of Liquid markup.

[1]: https://github.com/pepijndevos/pepijndevos.github.com/blob/master/_includes/code/nio.clj

If you have anything interesting to say about this code, NIO, webservers or Clojure, I'd love to hear from you.

{% highlight clojure %}
{% include code/nio.clj %}
{% endhighlight %}