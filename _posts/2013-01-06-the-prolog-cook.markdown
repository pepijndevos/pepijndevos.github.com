---
layout: post
title: The Prolog Cook
categories:
- clojure
- minikanren
- prolog
---

*brain dump alert*

I had read [this article on core.logic](http://clj-me.cgrand.net/2012/04/06/fair-conjunction-status-report/) a few days back, and while making myself a dinner, I came up with this analogy.

At first, I was confused about disjunction and conjunction, so let's start with the Prolog cook.

The prolog cook cooks depth-first. The cook has several recipes and steps to produce them. The first recipe on the list is an egg with ham and cheese.

The cook bakes the egg on top of the ham, adds some salt, finds out there is no cheese, throws everything away and moves on to the next recipe.

The next recipe is is a simple pasta. The cook checks if there is pasta, herbs, tomato, and finally checks to see how much water is in the tap.

Next up is the miniKanren cook. This cook has fair disjunction, but unfair conjunction.

So the cook looks at her recipes, picks the egg recipe, checks if there is an egg. Then, picks another recipe, like a pasta, checks if there is any pasta.

The cook keeps going over the recipes, discarding ones that can't be made. But at some point this cook will also check how much water is in the tap. And keep checking until there is no more water.

Finally, the fair cook, with fair conjunction.

This cook goes over his recipes the same way the miniKanren cook does, but upon checking for water, this cook opens the tap and continues cooking while keeping an eye on the tap.

Maybe the recipe can be adjusted not to check for all the water.   Even the fair cook is wasting a lot of water while doing other stuff, but at least she does other stuff in the meantime.

There have been very successful Prolog cooks, but not all recipes can be adjusted to work for the Prolog cook.