---
layout: post
title: Make all the Star Wars memes
image: /images/prequelmemes/1meme5.jpg
categories:
- python
- starwars
---

Normal people watch movies or play games to relax. Me on the other hand, I write code about movies or games to relax.

I came across some memes that posed the question if every line from the Star Wars prequels is meme material, accompanied by a shot from said prequel with a character confirming said question. Easily nerd-sniped as I am, I thought, surely there are lines in the movie that are completely boring?

I figured it should be doable to extract the subtitles from the movie, and use those to generate **every possible Star Wars meme**. Well, at least all the ones adhering to the format described above.

#### Extracting the subtitles and timestamps

It turns out `srt` subtitles are a pretty easy format to grep, but in case the subtitles are embedded inside the video file, or in some other binary format, `ffmpeg` got you covered. Once you have the `srt` file, a simple `grep` command can be used to extract the timestamps.

Note the extra space and lack of decimal point. I'm lazy, and this seemed the easiest way to get a timestamp that refers to a frame when the subtitle is displayed. This sometimes fails, so a mean between the start and end time is obviously better.

```
ffmpeg -y -txt_format text -i sw.mkv out.srt;
timestamps=$(grep -oE " [0-9]{2}:[0-9]{2}:[0-9]{2}" out.srt);
```

#### Generating images for every subtitle line

Again, `ffmpeg` can do the job, but it requires a bit more than a straight copy-paste from the manual. Let's go over the options after the full command, that I run inside a for loop.

```
ffmpeg -y -ss $ts -copyts -i sw.mkv -vf subtitles=sw.mkv -frames:v 1 frames/out$i.jpg;
```

* `-y` answer yes to all prompts.
* `-ss timestamp` seek to the specified timestamp. It is important this comes *before* the input file, otherwise it'll render the whole movie up to that point. However, doing so changes the timestamps, which we need for the subtitles.
* `-copyts` preserve the timestamp. This was a life-saver, thanks to the `ffmpeg` IRC channel.
* `-i` the input file...
* `-vf subtitles=file` specifies a filter that "burns" the subtitle into the movie.
* `-frames:v 1` save a single frame to the specified output file

#### Add the meme caption

With the hard part behind us, now we're back to straight copy-pasting from the ImageMagick manual. The only interesting bit I added is the `pointsize`.

```
convert frames/out$i.jpg -background White -pointsize 32 label:'Insert funny caption' +swap  -gravity Center -append memes/meme$i.jpg;
```

#### Putting it all together

The whole script can be found [here](https://gist.github.com/pepijndevos/600039b1e0a3cc2ea868d0967196c837). I did not expect much, but it was already at frame 6 that I was pleasantly surprised.

![Yes, of course](/images/prequelmemes/1meme5.jpg)

But then I thought

![Surely you can do better](/images/prequelmemes/2meme1173.jpg)

So I created a [Twitter bot](https://twitter.com/EndlessPrequel) and a [search page](http://wishfulcoding.nl/prequelmemes/search.php).

The Twitter bot is just an IFTTT applet that posts an image, served up by a mind-numbingly simple PHP script that I copied from somewhere.

For the search script, I wrote a [little Python script](https://gist.github.com/pepijndevos/600039b1e0a3cc2ea868d0967196c837) that converts the `srt` file to a `csv` file that I can import into a MySQL database, which I then query using `SELECT * FROM subtitles WHERE MATCH(sub) AGAINST ('words')`.

That's all for now.

![May the force be with you](/images/prequelmemes/1meme1287.jpg)
