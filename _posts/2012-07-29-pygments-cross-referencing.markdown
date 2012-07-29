---
layout: post
title: Pygments cross-referencing
categories:
- pygments
- python
---

I recently discovered the convenience of using ctags in VIM, to quickly check the definition of a function and go back to reading or writing.

While talking to some friends at Hacker School, we thought it would be super convenient to be abple to do the exact same thing while reading code on Github or other websites.

A few days and 45 LOC later, it became a reality in Pygments. It's like LXR you can actually use, and on nearly any language.

[The patch][1] is not yet accepted, so you'll have to use my fork for now.

You can see the cross-referenced source of the patch itself [here][2].

Pygments just handles files line-by-line, so it has no understanding itself of multiple files.

Rather, you generate the ctags file yourself(use the -n parameter!), tell pygments what the links should look like, and loop over your files with a few lines of bash.

{% highlight bash %}
{% include code/multipygmentize.sh %}
{% endhighlight %}

[1]: https://bitbucket.org/birkenfeld/pygments-main/pull-request/87/hyperlink-names-to-definitions-using-ctags
[2]: http://wtf.tw/etc/pygments/pygments/formatters/html.py.html
