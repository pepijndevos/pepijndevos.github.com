---
layout: post
title: Generative Music
categories:
- clojure
- markov
- music
---

It's been a long time since I played with [Overtone][1]. What I've always wanted to do is generate music automatically. Today I ran into [jFugue][2], which makes this really easy, listen to this:

<object height="81" width="100%"> <param name="movie" value="http://player.soundcloud.com/player.swf?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F18222911"></param> <param name="allowscriptaccess" value="always"></param> <embed allowscriptaccess="always" height="81" src="http://player.soundcloud.com/player.swf?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F18222911" type="application/x-shockwave-flash" width="100%"></embed> </object>
<span><a href="http://soundcloud.com/pepijndevossc/markov-generated-music">Markov generated music</a> by <a href="http://soundcloud.com/pepijndevossc">pepijndevos</a></span>

I just took some music from [wikifonia][3], constructed a Markov-chain and did a random note-walk.

This could work just as well with Overtone, but I used jFugue because it comes with a parser for [MusicXML][4] files.

To run the code below, you need to have jFugue on the classpath. Then just pipe a couple of notes from generate into play.

{% highlight clojure %}
{% include code/markov-music.clj %}
{% endhighlight %}

[1]: https://github.com/rosejn/overtone
[2]: http://www.jfugue.org/
[3]: http://www.wikifonia.org/
[4]: http://www.recordare.com/musicxml
