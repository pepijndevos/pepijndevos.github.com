---
author: pepijndevos
comments: true
date: 2012-04-23 01:49:59+00:00
excerpt: None
layout: post
link: http://studl.es/2012/04/generic-programming-tutorial/
slug: generic-programming-tutorial
title: Generic programming tutorial
wordpress_id: 279
categories:
- studl.es
- Code
- Tutorial
---

I have wanted to teach you some programming since I started this blog, but I refrained from doing so, because there are so many ways to program, and I want to use them all.

What I'm going to attempt, is not to teach you a programming <em>language</em>, but teach about programming itself. This may seem useless to you, but my experience is that when you understand programming, the language does not really mater, and you can program in anything that is not excessively weird.

For practical reasons, any code examples will be written in NBC, Python, NXT-G and Clojure.
<h3>A computer</h3>
To understand programming, you first need to understand what a computer is. For our purpose, it suffices to think of a computer as a processor, a block of storage, and a block of memory.

This is true for desktops, smartphones, the NXT, and anything else with these components. Of course there are some other components involved, which can be divided in inputs(keyboard, sensor) and outputs(screen, motor).

What a computer does, is read instructions from the storage, execute them on the processor, which modifies the memory and reads/writes to the in- and outputs.

To give you an idea, you could put instructions in the storage that tell the computer to store the number 2 in memory, copy it, and multiply the two numbers, saving the result in memory.

A more useful set of instructions could put the input from the keyboard to the screen, or read a sensor, do some math and control the motor.
<h3>A compiler</h3>
When I said instructions, I did not mean instructions in plain English. Processor instructions are not easy to read and write for humans, that is why we let computers translate them for us.

In its most basic form, a compiler is a set of instructions which converts words like "add" and "read" to stuff that a computer understands.

More advanced compilers also allow you to define new words, such as "turn left", in terms of other words, such as "motor on" and "motor off".
<h3>A language</h3>
A language is a set of instructions, as understood by a specific compiler. A language consists of a few things.
<h3>A syntax</h3>
This is like grammar for compilers. A few examples of adding a number:
<ul>
	<li>Python: 1 + 1</li>
	<li>Clojure: (+ 1 1)</li>
	<li>NBC: add 1 1 result</li>
</ul>
<div>Which are all the same thing, except that NBC is a statement instead of an expression, more about that later.</div>
<h3>An API</h3>
This is the hardest part of programming. But let me tell you, even good programmers don't remember all APIs, you simply google them.

An API is the set of words at your disposal to express your problem. This API is different for every language.

In NBC there is a word called "OnFwd" which can be used with an output, like "OnFwd(OUT_A)", but in Python, there is no such thing. Python doesn't even know what a motor is, or what forward means.

However, people have already defined words to talk about the NXT in Python. To use words already defined elsewhere, we can say "from nxt.motor import Motor, PORT_A" in Python. Now we can say "Motor(my_nxt, PORT_A).run()"
<h3>Expressions</h3>
Expression have a value. The value of (+ 1 1) is 2, so we can also say (* 2 (+ 1 1)), which has the value 4.

People commonly say expressions 'return' a value, which is what you do when you define an expression in Python:

{% highlight plaintext %}
def expressions():
    return 2
{% endhighlight %}

<h3>Statements</h3>
Unlike expressions, statements do not have a value. What would be the value of "while True:"(the start of a loop in Python)?

Note that not all languages have expressions and statements.

NBC has only statements, which is why you write "add 1 1 result", so that the result of the addition gets saved in memory.

Clojure has only expressions. if something has no useful value, it returns nil.

Python is a mixed bag.
<h3>References</h3>
So far I have talked about that block of memory as an abstract thing where you save and retrieve values. In reality, it is very useful to label the box you put it in.

For example, in Python you can say

{% highlight plaintext %}
x = 1
y = x + 1
{% endhighlight %}

These are statements that store the value of the expression. 'x' now references the value of 1, which is 1. 'y' is now a reference to the value of adding the value of x(1) to the value of 1(1).
<h3>Collections</h3>
So far we used numbers as values, but what if you want to talk about a collection of things?

languages usually provide means of defining a collection of things, and for doing things to the elements or the whole collection, like sorting it, or getting/setting elements.

An array of 5 integers in NBC:

{% highlight plaintext %}
dseg segment
  int reference[5]
dseg ends
{% endhighlight %}

<h3>Functions</h3>
We talked earlier about the words that make up an API. In most cases, words are also just references to values.

These values are usually called functions, and consist of a collection of other functions.

Not all languages have functions as values. In NBC, functions are statements, which don't have a value. Clojure, however...

{% highlight plaintext %}
(def x 2)
(def square (fn [n] (* n n)))
(square x)
{% endhighlight %}

Here, I defined 'x' a reference to the value 2, and 'square' a reference to a function that multiplies a number by itself. The, I called the value of 'square' with the value of 'x', resulting in the value 4.
<h3>Learning a programming language</h3>
I hope to have given you a good understanding of how a programming language works. To actually start using a language like NBC, you need to find out a few things:
<ul>
	<li>How do I use words/call functions?</li>
	<li>How do I define references to values in memory?</li>
	<li>How do I define new words?</li>
	<li>What existing words do I have at my disposal?</li>
</ul>
A good starting place is usually a beginners tutorial. Google for "&lt;language&gt; tutorial" and click the first result.

After a section or two, they usually start to talk about how to do things. You might continue, or stop here, and get your hands dirty. If you run into trouble, simply google for "&lt;language&gt; how to &lt;problem&gt;" or find the function reference by searching for "&lt;language&gt; function reference&gt;".

If you are really in deep trouble, <a href="http://stackoverflow.com/">Stack Overflow</a> is a great website for asking questions.

I hope this is enough preparation for you to start learning, and for me to focus on explaining how to do things in any language needed, like plotting a picture with the NXT ;-)