---
layout: post
title: clojure.core.logic magic square
categories:
- clojure
- core.logic
---

Last night my brother told me about this puzzle where you have a square of 3x3 that you have to fill with 1-9 in a way that all columns and rows sum up to 15.(this is not a real magic square, but anyway)

After discussig our strategies for a bit, we went on to think about generalising the problem to bigger squares.

The next morning we tested our theory in excel and produced a 4x4 square. The method we used was to fill in numbers at random, then switching them around horizontally until all collumns equalled 34 and finally switching them around vertically to make the rows work out.

Once we started thinking about doing the 5x5 grid, I suggested I might do it faster using core.logic. I was wrong.

{% highlight clojure %}
user=> (time (test/magic 3))
(1 5 9)
(6 7 2)
(8 3 4)
"Elapsed time: 451.880413 msecs"
nil
user=> (time (test/magic 4))
(1 2 15 16)
(7 8 9 10)
(12 11 6 5)
(14 13 4 3)
"Elapsed time: 6386.272944 msecs"
nil
user=> (time (test/magic 5))
OutOfMemoryError GC overhead limit exceeded
{% endhighlight %}

The code simply generates N<sup>2</sup> logic variables in the 1-9 domain that are distinct. These are then sliced up in rows and collumns and constrained to sum up to the "magic number".

{% highlight clojure %}
{% include code/magic.clj %}
{% endhighlight %}

I suspect I can still beat my brother to 6x6 by implementing one of the techniques outlined on wikipedia in a good old imperative style, especially since he's not even trying.

![graph](/images/geeks-vs-nongeeks-repetitive-tasks.png)
