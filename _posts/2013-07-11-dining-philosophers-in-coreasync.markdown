---
layout: post
title: Dining Philosophers in core.async
categories:
- clojure
- core.async
---

For the July [Amsterdam Clojure](http://amsclj.nl) meetup, a lot of people where curious what core.async is all about, so I proposed tackling the [dining philosophers problem](http://en.wikipedia.org/wiki/Dining_philosophers_problem) with core.async.

The problem is explained and a solution proposed in the [CSP book](http://www.usingcsp.com/cspbook.pdf) section 2.5, but using events rather than channels.

We worked on the problem dojo style, switching the driver seat every few minutes. But with no one really knowing the library very well, progress was slow, and by the end of the meetup we could make one philosopher eat.

One problem we ran into was that `go` blocks are lexical, so you can't easily write helper functions that use `<!`

{% highlight clojure %}
(defn putdown [ch] (>! ch :fork))
(go (putdown (chan)))
Exception in thread "async-dispatch-1" java.lang.AssertionError: Assert failed: >! used not in (go ...) block
{% endhighlight %}

So this morning I sat down to make this thing work.

During the meetup we had a function that would do something silly to setup a vector with 5 forks represented by channels, which I replaced by some equally silly, until I just came up with this.

I use channels with a buffer of one and put a fork in that buffer. This makes sure a fork can but picked up and put down without blocking, but only once.

{% highlight clojure %}
(defn set-table []
  (let [table [(chan 1) (chan 1) (chan 1) (chan 1) (chan 1)]]
    (doseq [ch table]
      (>!! ch :fork))
    table))
{% endhighlight %}

The butler making sure only 4 philosophers are seated is simply represented as 

{% highlight clojure %}
(chan 4)
{% endhighlight %}

This leads to the definition of the basic actions

{% highlight clojure %}
(def pickup <!!)
(def putdown #(>!! % :fork))

(def sit-down #(>!! % :sit))
(def get-up <!!)
{% endhighlight %}

The simplified behaviour of a philosopher then becomes

{% highlight clojure %}
(sit-down butler)
(alts!! my-forks)
(alts!! my-forks)
(println "eating")
(map putdown my-forks)
(get-up butler)
(println "thinking")
(recur)
{% endhighlight %}

And finally we can run 5 philos threads.

{% highlight clojure %}
(doseq [p philosophers]
  (thread (philosopher table butler p)))
{% endhighlight %}

Be sure to check out the [code](https://github.com/ams-clj/async-philosophers/blob/pepijn/src/async_test/core.clj) and [output](https://www.refheap.com/16496) and join the next meetup if you're in the Netherlands.