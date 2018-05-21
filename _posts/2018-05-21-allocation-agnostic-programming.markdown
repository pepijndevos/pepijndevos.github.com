---
layout: post
title: Allocation-agnostic programming
categories:
- clojure
- functionalprogramming
---

If you define a variable, you use code to generate that data.
This is the same concept that underlies atoms in Clojure:
Your change is expressed as a function that can be repeated until it succeeds.
So what if you maintain a relation between the data and the code that generated it?

This is kind of what happens in Haskell, with its lazy evaluation.
You don't really know if the thing you're using is evaluated.
But you do know it's only evaluated once, and kept around for as long as referenced.

What if you embrace functional purity, and allow deallocation and repeated evaluation?
Below is a silly Clolure function that implements a future that may throw away and recompute your result.
Variations of this theme are possible that compute in the current thread for less overhead,
or additionally wrap the future in `java.lang.ThreadLocal` to avoid thrashing the cache.
(Seems similar to [durable-ref](https://github.com/riverford/durable-ref) in a way)

{% highlight clojure %}
(defn soft-future-call [f]
  (let [sref (atom (java.lang.ref.SoftReference. (future-call f)))]
    (reify clojure.lang.IDeref
      (deref [this]
        (if-some [fut (.get @sref)]
          (deref fut)
          (do
            (reset! sref (java.lang.ref.SoftReference. (future-call f)))
            (deref this)))))))
{% endhighlight %}

I can't remember where I read it, but it is said that for single-core machines the fastest code reuses as much data as possible,
while on multi-core machines it is often faster to avoid sharing data, and recompute it locally.
It may also be interesting for larger-than-memory problems.
For example, compiling a huge codebase can use a lot or RAM.
Maybe it turns out that recomputing parts of the data is faster than relying on swap space.
So it's maybe an interesting paradigm to write code that abstracts away the distinction between a function and its result.

Well, it's just a rough idea.
Maybe it turns out to be really mundane, boring, and annoying.
Maybe it turns out to be really powerful, with the right tools and abstractions.
