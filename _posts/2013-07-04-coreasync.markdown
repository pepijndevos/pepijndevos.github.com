---
layout: post
title: core.async
categories:
- clojure
- core.async
---

I am reading about Clojure's core.async and the CSP book it is based on. A few things that stood out from [the announcing blog post](http://clojure.com/blog/2013/06/28/clojure-core-async-channels.html) and [the book](http://www.usingcsp.com/cspbook.pdf):

> It should be noted that Clojure's mechanisms for concurrent use of state remain viable, and channels are oriented towards the flow aspects of a system.

> This law which permits a system defined in terms of concurrency to be given an alternative description without concurrency.

So I would say core.async is a library for reasoning about sequential control flow in concurrent programs. That's a great idea.

A curious thing is that the first 3 chapters of the book discuss  events and only in chapter 4 are channels introduced. Core.async just includes the channels.

Compare:

> An event is an action without duration, whose occurrence may require simultaneous participation by more than one independently described process.

> We shall observe the convention that channels are used for communication in only one direction and between only two processes.

So channels have one sender and one receiver, while events just happen if all parties involved agree.

Observe the following orchestra

{% highlight clojure %}
(defn player [state]
  (assert (= :swing (<!! state)))
  (>!! state :note)
  (println "fuut")
  (recur state))

(defn conductor [state]
  (Thread/sleep 1000)
  (>!! state :swing)
  (assert (= :note (<!! state)))
  (recur state))

(def state (chan))
(thread (conductor state))
(thread (player state))
;> fuut
;> fuut
;> fuut
;> ...
{% endhighlight %}

I would really like to have a pattern on receiving, so that instead of assert you could just say "receive X" and you won't get a Y you can't deal with.

The book describes channels as an event of the pair c.v where c is the name of the channel and v is the value of the message. Like any event, the communication only happens when both ends agree on the event.

{% highlight clojure %}
(defn player [state]
  (<!! state :swing)
  (>!! state :note)
  (println "fuut")
  (recur state))

(defn conductor [state]
  (Thread/sleep 1000)
  (>!! state :swing)
  (<!! state :note)
  (recur state))

(def state (chan))
(thread (conductor state))
(thread (player state))
{% endhighlight %}

But the bigger problem with this program is that it doesn't work for more than one player. This could be solved with a broadcast channel, or by using events.

{% highlight clojure %}
(defn player [state]
  (!! state :swing)
  (!! state :note)
  (println "fuut")
  (recur state))

(defn conductor [state]
  (Thread/sleep 1000)
  (!! state :swing)
  (!! state :note)
  (recur state))

(def state (events))
(thread (conductor state))
(thread (player state))
(thread (player state))
(thread (player state))
{% endhighlight %}

Maybe I misunderstood the book, or maybe this is just out of scope for core.async, but I think it'd be neat to have events *and* channels.

{% highlight clojure %}
(defn conductor [swing]
  (loop []
    (Thread/sleep 1000)
    (.await swing)
    (recur)))

(defn player [swing sound]
  (loop []
    (.await swing)
    (println sound)
    (recur)))

(def swing (java.util.concurrent.CyclicBarrier. 4))
(future (conductor swing))
(future (player swing "fuut"))
(future (player swing "fwoop"))
(future (player swing "bang"))
;> fuut
;> fwoop
;> bang
;> ...
{% endhighlight %}