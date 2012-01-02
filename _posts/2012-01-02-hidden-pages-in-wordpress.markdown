---
layout: post
title: Hidden Pages in Wordpress
categories:
- wordpress
---

More often that not, I need to hide pages from the menu and search pages, when setting up a Wordpress blog.

Usually for download and thank-you pages, or other stuff that is only accessible via a direct link.

By default, Wordpress includes a visibility settings, which has 3 values.

* Public -- anyone can see it.
* Protected -- Anyone with the password can see it.
* Private -- Only you can see it

None of them is useful for my goals, however, some digging revealed that a small change would allow you to link directly to password protected pages.

Copy wp-pass.php to unlock.php, and make the changes below.

{% highlight diff %}
{% include code/unlock.diff %}
{% endhighlight %}

Now you can simply link to <http://example.com/unlock.php?post_password=foobar&return=/thank-you/>