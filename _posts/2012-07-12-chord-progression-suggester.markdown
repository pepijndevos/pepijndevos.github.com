---
layout: post
title: Chord Progression Suggester
categories:
 - python
 - music
 - tkinter
 - mingus
---

I did this project together with Sarah, the goal is to suggest good chord progressions to musicians.

![GUI screenshot](/images/chordparser.png)

We both know *some* music theory, so we started out by reading about what makes a good chord progression. The longer you look at it, the more complicated it gets. I quickly concluded that music is art, not science, so you can do whatever you want.

Instead, we wrote a scraper for guitar tabs. Just download a ton of them and look for anything that looks roughly like 

    ([A-Ga-g][b#]?)(m)?((?:maj)?[0-9])?(sus[0-9]|add[0-9])?(/[A-Ga-g][b#]?)

We then try to figure out which key the original song was in, and convert the chords to intervals, called universal notation.

Now that we have these sequences of legit chord progressions, we need to persist them in a smart way.

We went through markov chains, tries and some other crazyness, but fnally settled on just a flat list. It turns out just scanning the whole list for the given pattern takes an acceptable amount of time.

The final step was to write a gui that could suggest chords for you. It's written in Tkinter and uses Mingus for some playing and parsing.

[Get my new pop hit][0]

[Get the source][1]

[0]: /images/song.midi
[1]: https://github.com/HackerSchool12/chordparser
