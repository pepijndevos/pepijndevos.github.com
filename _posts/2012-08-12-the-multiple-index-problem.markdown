---
layout: post
title: The Multiple Index Problem
categories:
 - clojure
 - haskell
 - functionalprogramming
---

I would like to name this problem in functional programming. There has been some talk aout how Clojure solves the [Expression Problem][0], but th Multiple Index Problem has deserved so little attention it doesn't even have a name.

A simple example of multiple indices can be seen in relational databases, where you can querry a table like `SELECT * FROM foo WHERE id=1` but you can add an index to the table and querry on other fields like `SELECT * FROM foo WHERE name="Pepijn"`.

If you would translate this to hash maps, in imperative languages, you could do the following

{% highlight python %}
{% include code/multipleindex.py %}
{% endhighlight %}

If you want to do this in a functional language, you're out of luck. In the most basic case, you'd have to add some indirection.

{% highlight clojure %}
{% include code/multipleindex.clj %}
{% endhighlight %}

Next up, I'll show 2 cases where this is a real pain, rather than just anoying.

#### Dijkstras Algorithm

Dijkstras algorithm is used to find the shortest path in a graph. It is fairly easy to implement using a vector, but also very slow.

Every itteration you need to find the closest unexplored node. With he flat list, you need to walk them all.

The way to solve this is to use a [Fibonacci Heap][1], which has constant time and logarithmic time opperations for getting and decrementing the lowest key.

However, it has *no* methods for lookingg up other keys, other than... walking the tree? The way this is done in imparative languages is to just have pointers to the nodes, but in a functional language, youre stuck walking the tree.

This defeats the whole purpose of using a Fibonacci Heap. Is it at all possible to implement Dijkstra in a pure functional way in an optiomal time complexity?

I ended up using a mutable heap and just not decrementing keys. Yuk!

You can see the code me and Mary wrote at Hacker School at [her github][2](fibbonaccy and flat array), [and mine][3](Using PriorityBlockingQueue)

#### Collision Detection

I struggled with the same problem in my Clojure game engine, before I realised this was a repeating pattern.

So I started out modeling my world as a list of objects, but quickly found that the bat needed to know where the ball was, so I turned the world into a hash map of IDs to objects, and all was good.

Until I needed to do collision detection on more than a dozen objects. If you just compare all objects to al others to see if they collide, you're doing O(n<sup>2</sup>), which is no good.

Lukily there are datastructures that do spatial indexing. Indexing... see my problem?

I wrote a [Quadtree][4] in Clojure, but as soon as I stuck all my objects in the tree, there was no way to look them up by ID.

I think I ended up doing some black magic to sorted maps to have lookup by ID and okayish collision detection.

That is all I have to say about the problem. I don't have a soltuion or a satisfying workaround. You can try adding intermediate indices, or introducing mutability, but it's painfull.

[0]: http://c2.com/cgi/wiki?ExpressionProblem
[1]: http://en.wikipedia.org/wiki/Fibonacci_heap
[2]: https://github.com/maryrosecook/dijkstra
[3]: https://github.com/pepijndevos/dijkstra-clj
[4]: http://en.wikipedia.org/wiki/Quadtree
