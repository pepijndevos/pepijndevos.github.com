---
layout: post
title: AppleScript tiling windows
categories:
- mac
- applescript
---

On Linux I use all sorts of key combos to tile windows, even though I no longer use a real tiling window manager.

On Mac OS X my windows are still mostly stacked and all over the place in a big mess. Mac doesn't even have a proper fullscreen button, other than you know… making it a space on its own.

I know there are tools to do the same thing in a more fancy way, but instead I wrote a few AppleScripts to split and maximise windows.

To use them, I placed them in my Scripts folder and enabled the AppleScript menu in the preferences of the AppleScript Editor.

![AppleScript Editor preferences](/images/applescript_preferences.png)

{% highlight applescript %}
{% include code/split.applescript %}
{% endhighlight %}

{% highlight applescript %}
{% include code/fullscreen.applescript %}
{% endhighlight %}