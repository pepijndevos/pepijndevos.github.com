---
layout: post
title: Archaic API helpers
categories:
- python
- rest
---

I've been working with a number of HTTP API's using Python recently, and have accumulated some helper functions that I hope you don't need.

### PHP

It might be possible to write a *good* REST API in PHP, but it's certainly not easy… or common.

#### Array parameters

Lets say your brilliant API design requires an object to be passed as a parameter. How do you go about this? Hint: It's not JSON.

Either you call `serialise` on the object and send it, or you use this other PHPism that lets you say `?param[field]=val`.

In the first case, use the [phpserialise](https://pypi.python.org/pypi/phpserialize) package, in the second case, use this handy pre-processing function:

{% highlight python %}
{% include code/apihelpers/flatten.py %}
{% endhighlight %}

### XML

I heard Java people like XML, so your API should probably offer it as an alternative to JSON, serialised PHP and INI files.

I was surprised there is no such thing as `xml.dumps` in Python, so I wrote it. There is no 1-on-1 mapping between dicts and XML, but this a *a* way to go from Python data to XML.

{% highlight python %}
{% include code/apihelpers/xmlgen.py %}
{% endhighlight %}

### INI

That was no joke. Take a look at [configparser](http://docs.python.org/3/library/configparser.html) if you ever encounter this.

### The internet is s-l-o-w

So take this handy Redis-based memoization function with you, and twiddle with the TTL.

{% highlight python %}
{% include code/apihelpers/rmem.py %}
{% endhighlight %}