---
layout: post
title: Mature HTTP client for asyncio
categories:
- python
- asyncio
- http
---

I have recently started writing a server for some obscure protocol using Python’s new asyncio module.

The module works great for writing the server, but any external IO the server has to do is tricky. There are simply not so many libraries, and asyncio doesn’t do patching the way gevent does.

The quick and dirty solution is to use [`run_in_executor`](https://docs.python.org/3/library/asyncio-eventloop.html#asyncio.BaseEventLoop.run_in_executor) to run blocking code in a thread.

The only other game in town for HTTP is [aiohttp](https://github.com/KeepSafe/aiohttp), which is relatively young and occasionally buggy.

Then I found that Tornado has support for running on the asyncio event loop. Tornado includes a much more mature HTTP client that can optionally use libcurl.

The Tornado HTTP client returns a Future that is similar but not compatible with Futures from asyncio. So in order to use Tornado in a asyncio coroutine, a little wrapper is needed.

{% highlight python %}
from tornado.platform.asyncio import AsyncIOMainLoop
from tornado.httpclient import AsyncHTTPClient
import asyncio

# Tell Tornado to use the asyncio eventloop
AsyncIOMainLoop().install()
# get the loop
loop = asyncio.get_event_loop()
# the Tornado HTTP client
http_client = AsyncHTTPClient()

# wrap the Tornado callback in a asyncio.Future
def aio_fetch(client, url, **kwargs):
    fut = asyncio.Future()
    client.fetch(url, callback=fut.set_result, **kwargs)
    return fut

# enjoy
@asyncio.coroutine
def main():
    print("fetching my site")
    mysite = yield from aio_fetch(http_client, "http://pepijndevos.nl/")
    print("my site said", mysite.reason)
    print("hello httpbin")
    httpbin = yield from aio_fetch(http_client, "http://httpbin.org/get?code=%d" % mysite.code)
    print(httpbin.body.decode())

print(loop.run_until_complete(main()))
{% endhighlight %}