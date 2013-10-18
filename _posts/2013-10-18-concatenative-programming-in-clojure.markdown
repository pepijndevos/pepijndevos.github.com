---
layout: post
title: Concatenative programming in Clojure
categories:
- clojure
- forth
---

Just before I went to EuroClojure I read about [Gershwin](http://gershwinlang.org/), a concatenative programming language based on Clojure.

With a lot of time to think and code on the train between Amsterdam and Berlin and back, I thought: Why make this a language at all?

Using the threading macro you can make a basic stack program as follows.

{% highlight clojure %}
(def add [stack]
  (conj (drop 2 stack)
        (apply + (take 2))))

(-> () ; the stack
  (conj 1)
  (conj 2)
  add)
;; => (3)
{% endhighlight %}

After that, things got interesting. What if I used a queue instead of a stack? What if I used any number of queues and stacks?

I switched to using [thrush](http://blog.fogus.me/2010/09/28/thrush-in-clojure-redux/), wrote a function to generate words from Clojure functions, and did some magic to apply words to multiple stacks.

{% highlight clojure %}
(defn nth-stack [n f]
  #(apply update-in % [n] f %&))

(thrush [() ()]  ; data stack and return stack
  (lit 2) dup c+ ; add numbers
  (lit 4) c=     ; verify result
  >r)            ; put bool on return stack
;; => [() (true)]
{% endhighlight %}

What if I used `clojure.core.reducers/fold` instead of `reduce` in `thrush`? Could I use dataflow programming and continuations to parallelize programs?

![mind blown](/images/mind-blown.gif)

I failed at parallelization, but I surely achieved dataflow programming.

The idea is that you have a contiunation-passing-style function for popping of the stack. That function either calls the continuation with the head and tail of the stack, or returns a negative stack which will call the continuation when an item is `conj`'d.

{% highlight clojure %}
(deftype NegStack [popfn stack]
  clojure.lang.ISeq
  ...
  (cons [_ o] (popfn o stack))
  ...)

(defn mpeek [stack cont]
  (if (seq stack)
    (cont (first stack) (next stack))
    (NegStack. cont stack)))

(defn add [stack]
  (mpeek stack (fn [car cdr]
    (mpeek cdr (fn [cdar cddr]
      (conj cddr (+ car cdar)))))))

(thrush () (lit 2) add (lit 2))
;; => (4)
{% endhighlight %}

Yes, you can have prefix, infix, postfix and otherfix all you want. But not on a fork-join pool.

I had it all figured out, including concatenating negative stacks. It's just not conceptually sound. Imagine this program

{% highlight clojure %}
(thrush () (lit 2) (lit 2) mul (lit 5) (lit 6) add add)
;; => (15)
{% endhighlight %}

Using my fork-join-based `pthrush` thish might be split up as

{% highlight clojure %}
(mconcat
  (thrush () (lit 2) (lit 2))
  (thrush () mul (lit 5) (lit 6) add add))
{% endhighlight %}

The first block produces `(2 2)`, the execution of the second block is more tricky.

1. `mul` produces a negative stack
2. 5 and 6 are `conj`'d onto this stack, resulting in `(30)`
3. The `add`'s consume 30 and produce a negative stack
4. This negative stack is concatenated to `(2 2)`, resulting in `(34)`

The problem is that `mul` got applied to `(5 6)` instead of `(2 2)` because of how the program was split.

It might still be possible to paralellize by analyzing the stack effect of words, but dataflow is definitely not the way to go.

All my experiments so far can be found on Github at [pepijndevos/staack](https://github.com/pepijndevos/staack)
