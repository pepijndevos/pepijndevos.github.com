---
layout: post
title: NXT Forth compiler in Clojure
categories:
 - forth
 - clojure
 - compiler
 - nxt
---

My first project at [Hacker School][0] was to write a compiler for Forth for the LEGO NXT. There are a few real programming languages for the NXT, but before I found [Mirah][1], I never really enjoyed them.

Initially, I wanted to write a Lisp, but the NXT bytecode is so static that even the concept of a cons cell is not viable.

Forth is a very simple stack based language that uses just that, a stack and subroutines(called words). Surpisingly, the only dynamic feature in the NXT are resizable arrays. I think it's a good fit.

This is the complete stack implementation in NBC.

{% highlight c %}
#define push(stack, val) \
  replace stack.data stack.data stack.offset val \
  add stack.offset stack.offset 1

#define pop(stack, val) \
  sub stack.offset stack.offset 1 \
  index val stack.data stack.offset
{% endhighlight %}

My implementation follows the [Boostrapping a Forth in 40 lines of Lua code][2] approach of defining a Forth word that evals the host language.

The only difference is that this Forth is compiled, so there is a word for Clojure and one for NBC.

The main Clojure file just defines an atom to store Forth words, and defines the clj word, which evaluates a Clojure expression.

As soon as that is in place, you're in Forth land.

First thing we do in Forth is some more Clojure defining ":\*" to mean "define clojure word", and then using ":\*" to define 3 more words.

 * ":" start of a Forth word
 * ";" end of a Forth word
 * "nbc" write a line of assembly

What follows are a few forth words defined in assembly. At this point, the following works

{% highlight factor %}
: square
  dup *;

2 square dot
{% endhighlight %}

[Get the code][3]

[0]: https://www.hackerschool.com/
[1]: http://www.mirah.org/
[2]: http://angg.twu.net/miniforth-article.html
[3]: https://github.com/pepijndevos/beauforth
