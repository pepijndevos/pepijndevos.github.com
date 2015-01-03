---
layout: post
title: Branch-free FizzBuzz in Assembly
categories:
- fizzbuzz
- assembly
---

I came across [this post](http://gigasquidsoftware.com/blog/2014/11/13/clojure-fizzbuzz-without-conditionals/) that discusses ways to write FizzBuzz in Clojure without using conditionals. However, most if not all of the solutions still do a lot of branching behind the scenes. Think of hash lookups for example.

So I asked to myself, how can I write a FizzBuzz solution with no branches at all?
Probably not in Clojure; you can't easily tell where its branching or not.

The only way to be absolutely sure is to write it in assembly. So I did.
I never did assembly before, so it might be terrible code.

I used an array of 15 pointers to either "fizz", "buzz", "fizzbuzz", or a number buffer.
I then filled the number buffer with the current number in ascii and printed whatever I get from the array.

One thing I struggeled with is how to stop.
At first I had *one* condition to see if I reached 100.
Now I use a lookup table that calls `sys_time` 99 times and then `sys_exit`. 

{% highlight nasm %}
{% include code/fizzbuzz.asm %}
{% endhighlight %}

To compile on a 64 bit machine:

{% highlight bash %}
nasm -f elf64 fizzbuzz.asm
ln -o fb fizzbuzz.o
./fb
{% endhighlight %}

