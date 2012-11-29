---
layout: post
title: Pure CSS Gauge
categories:
- css
- gauge
- animation
---

I got [nerdsniped](http://xkcd.com/356/)

<blockquote class="twitter-tweet"><p>still looking for pure css gauges, ideas?</p>&mdash; Almog Melamed (@radagaisus) <a href="https://twitter.com/radagaisus/status/273819647168626688" data-datetime="2012-11-28T16:04:25+00:00">November 28, 2012</a></blockquote>
<script src="//platform.twitter.com/widgets.js" charset="utf-8"> </script>

So I had to make this gauge using CSS animation.

<iframe style="width: 100%; height: 300px" src="http://jsfiddle.net/Xj6ua/1/embedded/" allowfullscreen="allowfullscreen" frameborder="0"> </iframe>

I used two nested divs with round borders. The outer one does the quadrants with a top/left/bottom/right border. The inner one has only a top border, and slides over the bottom one.

Note that halfway through, the inner border changes from sliding out, to sliding in. Otherwise you'd get in trouble during the last quadrant.

A good way to see how it works is removing the negative margin, or change the inner border colour.