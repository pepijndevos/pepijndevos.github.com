---
layout: post
title: Redis Pipelining
categories:
- redis
- python
---

I’d like to announce [Pypredis](https://github.com/pepijndevos/pypredis), a Python client for Redis that tries to answer the question

> How fast can I pump data into Redis?

There are many answers to that question, depending on what your goal and constraints are. The answer that Pypredis is exploring is pipelining and sharding. Let me explain.

The best use case is a few slow and independent commands. For example, a couple of big `SINTER` commands.

The naive way to do it using [redis-py](https://github.com/andymccurdy/redis-py) is to just execute the commands one after the other and wait for a reply.

    r = redis.StrictRedis()
    r.sinter('set1', 'set2')
    r.sinter('set3’, 'set4’)
    r.sinter('set5’, 'set6’)
    r.sinter('set7’, 'set8’)

In addition to the CPU time, you add a lot of latency by waiting for the response every time, so a better solution would be to use a pipeline.

    r = redis.StrictRedis()
    pl = r.pipeline()
    pl.sinter('set1', 'set2')
    pl.sinter('set3’, 'set4’)
    pl.sinter('set5’, 'set6’)
    pl.sinter('set7’, 'set8’)
    pl.execute()

That is pretty good, but we can do better in two ways.

First of all, redis-py does not start sending commands until you call `execute`, wasting valuable time while building up the pipeline. Especially if other work is done in-between Redis commands.

Secondly, Redis is — for better or worse — single-threaded. So while the above pipeline might use 100% CPU on one core, the remaining cores might not be doing very much.

To utilise a multicore machine, sharding might be employed. However, sequentially executing pipelines on multiple Redis servers using redis-py actually performs worse.

    pl1.execute() #blocks
    pl2.execute() #blocks

The approach that Pypredis takes is to return a [Future](https://docs.python.org/3/library/concurrent.futures.html#concurrent.futures.Future) and send the command in another thread using an event loop.

Thus, pipelining commands in parallel to multiple Redis servers is a matter of not waiting for the result.

    eventloop.send_command(conn1, “SINTER”, "set1", "set2")
    eventloop.send_command(conn2, “SINTER”, "set3”, "set4”)
    eventloop.send_command(conn1, “SINTER”, "set5”, "set6”)
    eventloop.send_command(conn2, “SINTER”, "set7”, "set8”)

A very simple [benchmark](https://github.com/pepijndevos/pypredis/blob/master/test.py) shows that indeed Pypredis is a lot faster on a few big and slow commands, but the extra overhead makes it slower for many small and fast commands.

    pypredis ping
    1.083333
    redis-py ping
    0.933333
    pypredis sunion
    0.42
    redis-py sunion
    11.736665

