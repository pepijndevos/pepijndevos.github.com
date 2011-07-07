---
layout: post
title: Can't take value of a macro
categories:
- clojure
- dsl
- macro
---

Do. not. ever. make me see that exception. Because in my opinion that means you failed at designing your shiny DSL.

A macro is not a thing, but a transform from one thing to another. If your DSL does not have things -- domain specific values, as [Christophe Grand calls them][1] -- at its core, you're doing it wrong.

First and foremost, a macro should be simple, where simple means 'not compound', as [explained by Stuart Halloway][2].

I would like to define a simple macro as one that is written as a compound of simple functions returning code that evaluates to a simple or composite value.

 * `(+ 1 1)` returns a simple value
 * `(a-record. 1 2 3)` evaluates to a composite of simple things
 * `(do (def foo 2) (def bar 4) (do-thing-to foo))` is not simple

Another good non-reason to use macros is to defer execution.

{% highlight clojure %}
(defmacro assoc-once [m k v]
  `(if-not (get ~m ~k)
     (assoc ~m ~k ~v)
     ~m))

(assoc-once {:a 1} :a (println "foo"))
; {:a 1}
(assoc-once {:a 1} :b (println "foo"))
; foo
; {:b nil, :a 1}
{% endhighlight %}

Works nicely, uh? Now assume we have an atom, try `(swap! a assoc-once ...)` It'll honor our post title: {{ page.title }}

You know what else defers execution? A function.

{% highlight clojure %}
(defn assoc-once [m k v]
  (if-not (get m k)
    (assoc m k (v)) ; note parens
    m))

(assoc-once {:a 1} :a #(println "foo"))
; {:a 1}
(assoc-once {:a 1} :b #(println "foo"))
; foo
; {:b nil, :a 1}
{% endhighlight %}

This is of course not as good-looking as the macro, but the `swap!` case works fine here. If you care enough, you could define a macro on top of the function, giving you a simple macro.

{% highlight clojure %}
(defmacro assoc-once [m k v]
  `(assoc-once* ~m ~k (fn [] ~v)))
{% endhighlight %}

Now, this is a very long and opinionated post for me already, so I'm going to stop here. I highly recommend that you watch the 2 video's I linked to.

I had in mind to take you through another example of using protocols and function composition to simplify and avoid macros, but I'll just give you [this][3], [this][4] and [that][5] as a homework assignment.

[1]: http://blip.tv/clojure/christophe-grand-not-dsl-macros-4540700
[2]: http://blip.tv/clojure/stuart-halloway-simplicity-ain-t-easy-4842694
[3]: http://dev.clojure.org/jira/secure/attachment/10246/0001-rel-defrel-extend-rel.patch
[4]: http://pragprog.com/magazines/2011-07/growing-a-dsl-with-clojure
[5]: /clojure-micro-pattern-matcher/index.html
