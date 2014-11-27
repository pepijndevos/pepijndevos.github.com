---
layout: post
title: Constraint Driven Development
categories:
---
I was reflecting on all the fun I'm having with my Arduino GPS system,
and found something interesting.

I could have used a Raspi, and have some actual RAM, storage and screen.
But by constraining myself to the bare minimum,
I created all sorts of wonderful algorithms and hacks.

This is maybe a known thing, but it bears repeating: constraints foster creativity.
Writing "a story" is much harder than wiritng "a story about the first brick of the city hall of Amsterdam".
By narrowing down the search space, your brain can do a much more exhaustive search.

An example that comes to mind is LuaJIT.
Lua is found in all sorts of places, so a small binary size is a virtue.
Someone told me it fits on a floppy, which I found is true.

![LuaJIT on a floppy](/images/luajit_floppy.png)

Consequently, it contains wonderful things like this.

    
 > [A recursive backpropagation algorithm with backtracking, employing
 > skip-list lookup and round-robin caching, emitting stack operations
 > on-the-fly for a stack-based interpreter -- and all of that in a meager
 > kilobyte? Yep, compilers are a great treasure chest. Throw away your
 > textbooks and read the codebase of a compiler today!]
    

Once upon a time, Guy Steele set a high tax on special forms (syn-tax, ha, ha...),
and consequently invented Scheme.
Later, John Shutt thought that wasn't enough, and invented Kernel.
Clojure on the onther hand, is only constrained to be practical.

What is your favourite piece of constrained software?
How do you set constraints for yourself?
