---
author: pepijndevos
comments: true
date: 2012-05-29 08:22:05+00:00
excerpt: None
layout: post
link: http://studl.es/2012/05/mirah-on-nxt/
slug: mirah-on-nxt
title: Mirah on the NXT
wordpress_id: 319
categories:
- studl.es
---

I'm working on a few things that require a bit more code that what I've done so far, but I find it to cumbersome to write so much code using NBC, NXC or NXT-G. Then I found <a href="http://lejos.sourceforge.net/">Lejos</a>.

Lejos is a Java Virtual Machine for the NXT, and it replaces the standard firmware. While Java is a marginally nicer language, the major win is in the packages.

Lejos comes with a lot of packages that contain useful code for doing common things, neatly organized by names like <a href="http://lejos.sourceforge.net/nxt/nxj/api/lejos/robotics/navigation/Navigator.html">lejos.navigation.Navigator</a> or <a href="http://lejos.sourceforge.net/nxt/nxj/api/java/util/Map.html">java.util.Map</a>.

The only problem with Java is that it's verbose. Compare a simple program that prints "Hello world!" written <a href="http://rosettacode.org/wiki/Hello_world/Text#Ruby">in Ruby</a>, and <a href="http://rosettacode.org/wiki/Hello_world/Text#Java">in Java</a>.

You might wonder why Ruby is suddenly involved. <strong>It's because of Mirah, which is Ruby syntax for Java classes. </strong>You could thus run the linked Ruby code, but you are actually using java.lang.System.out.println.

This means that if you need to know how to write something, google for the Ruby solution. If you want to know which classes to use, look for the Java solution. (classes are like hierarchical  collections of words, more on that later)

To make this all work with the NXT, you first need to <a href="http://lejos.sourceforge.net/nxt/nxj/tutorial/Preliminaries/GettingStarted.htm">instal Lejos</a>.

To install Mirah, you also need to <a href="http://jruby.org/getting-started">install jRuby</a>, afterwards you can just run

{% highlight plaintext %}
jruby -S gem install mirah
{% endhighlight %}

Stay tuned for for some Mirah code for the NXT. I might also tell you more about classes.