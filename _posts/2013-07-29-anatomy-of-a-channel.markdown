---
layout: post
title: Anatomy of a Channel
categories:
- clojure
- core.async
---

I have been trying to implement a CyclicBarrier in core.async, but it took me a while to understand how a channel works.

To support IOC and thread and be fast, the implementation is a lot more verbose and complicated than the bunch of promises I initially expected.

It seems all verbosity and callback hell that can be avoided by using channels is bundled in their implementation. Take this loop from `cleanup` that removes inactive handlers, can you spot the 2 actual lines of code?

{% highlight clojure %}
(when-not (.isEmpty takes)           ;bla
  (let [iter (.iterator takes)]      ;bla
    (loop [taker (.next iter)]       ;bla
      (when-not (impl/active? taker) ;inactive?
        (.remove iter))              ;KILL!
      (when (.hasNext iter)          ;bla
        (recur (.next iter))))))     ;bla
{% endhighlight %}

But let's go back to the beginning. The basic unit of a channel is a handler, a glorified callback. It has a commit method with *returns* the callback, an id for avoiding deadlock and a method to check if it is active.

The latter is used with `alts!!`, so that after one possible channel has been acted upon, the other handlers turn inactive, but lets stick to a simple `<!!`.

{% highlight clojure %}
(defprotocol Handler
  (active? [h] "returns true if has callback. Must work w/o lock")
  (lock-id [h] "a unique id for lock acquisition order, 0 if no lock")
  (commit [h] "commit to fulfilling its end of the transfer, returns cb. Must be called within lock"))

;; no locking, always active.
(defn- fn-handler
  [f]
  (reify
   Lock
   (lock [_])
   (unlock [_])
   
   impl/Handler
   (active? [_] true)
   (lock-id [_] 0)
   (commit [_] f)))
{% endhighlight %}

Now let's look at `<!!` itself, which is pretty simple. It creates a promise and a callback that delivers the promise. It then calls `take!` with it. If `take!` returns nil, deref the promise, otherwise deref the box.

{% highlight clojure %}
(defn <!!
  "takes a val from port. Will return nil if closed. Will block
  if nothing is available."
  [port]
  (let [p (promise)
        ret (impl/take! port (fn-handler (fn [v] (deliver p v))))]
    (if ret
      @ret
      (deref p))))
{% endhighlight %}

Now let's look at the main course, `ManyToManyChannel`.

If you look through all the verbosity, `take!` and `put!` are pretty similar. There are 3 main code paths in each.

1. There is someone at the other end. Match up the handlers, call their callbacks and return the value or `nil` in an `IDeref`.
2. There is room in the buffer. Take/put the value in the buffer and return the value or `nil` in an `IDeref`.
3. There is no room. Put the handler in a list and return `nil`.

{% highlight clojure %}
(take!
 [this handler]
 (.lock mutex)
 ; remove all inactive handlers
 (cleanup this)
 (let [^Lock handler handler
       ; get the actual callback if it is active
       commit-handler (fn []
                        (.lock handler)
                        (let [take-cb (and (impl/active? handler) (impl/commit handler))]
                          (.unlock handler)
                          take-cb))]
   ; If there are items in the buffer,
   ; take one, put it in a box and return it.
   ; We now have a free spot in the buffer
   ; so we try to find an active put handler.
   ; if we find one, put its value in the buffer
   ; and commit its callback.
   (if (and buf (pos? (count buf)))
     (do
       (if-let [take-cb (commit-handler)]
         (let [val (impl/remove! buf)
               iter (.iterator puts)
               cb (when (.hasNext iter)
                    (loop [[^Lock putter val] (.next iter)]
                      (.lock putter)
                      (let [cb (and (impl/active? putter) (impl/commit putter))]
                        (.unlock putter)
                        (.remove iter)
                        (if cb
                          (do (impl/add! buf val)
                              cb)
                          (when (.hasNext iter)
                            (recur (.next iter)))))))]
           (.unlock mutex)
           (when cb
             (dispatch/run cb))
           (box val))
         (do (.unlock mutex)
             nil)))
     ; There is nu buffer to take from,
     ; so we search for an active putter
     ; for each putter check if both ends are active.
     ; If so, remove it from the list and let the callbacks and value,
     (let [iter (.iterator puts)
           [take-cb put-cb val]
           (when (.hasNext iter)
             (loop [[^Lock putter val] (.next iter)]
               ; funny bit where deadlock is avoided
               (if (< (impl/lock-id handler) (impl/lock-id putter))
                 (do (.lock handler) (.lock putter))
                 (do (.lock putter) (.lock handler)))
               (let [ret (when (and (impl/active? handler) (impl/active? putter))
                           [(impl/commit handler) (impl/commit putter) val])]
                 (.unlock handler)
                 (.unlock putter)
                 (if ret
                   (do
                     (.remove iter)
                     ret)
                   (when-not (impl/active? putter)
                     (.remove iter)
                     (when (.hasNext iter)
                       (recur (.next iter))))))))]
       ; if we found 2 callbacks in the previous step
       ; immediately return the value in a box and commit the putter.
       ; if the channel is closed, return nil in a box.
       ; if the channel is open and there is no matching callback
       ; add the handler to the list of takers
       ; and return nil, without a box.
       (if (and put-cb take-cb)
         (do
           (.unlock mutex)
           (dispatch/run put-cb)
           (box val))
         (if @closed
           (do
             (.unlock mutex)
             (if-let [take-cb (commit-handler)]
               (box nil)
               nil))
           (do
             (.add takes handler)
             (.unlock mutex)
             nil)))))))
{% endhighlight %}

I wonder if the code could be made more readable with a few macros for locking and iterators.

Understanding `put!` is left as a exercise for the reader. Understanding the IOC part is left as an exercise for the Clojure gods.

I will write more about my ideas for barriers and transactions later.
