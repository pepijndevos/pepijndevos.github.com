---
layout: post
title: "Lithopedia part 1: intro and clay experiments"
categories:
- lithopedia
- cnc
---

In this digital age so much of our information is _only_ on the internet, and it's hard to predict how much of it would survive another [Carrington event](https://en.wikipedia.org/wiki/Carrington_Event). So what if we engrave all of the information on the internet in stone? We have clay tablets dating back thousands of years, so if we want to preserve our knowledge for the millenia to come, it's a time-proven method.

That's it. That's the plan. "set in stone" Wikipedia, literally. I know it sounds crazy and I don't have any illusions about completing the project, but it seems a fun mix of art project and potentially my biggest contribution to the persistence of human kind haha

But how do I go about doing this? I want to automate it as much as possible, but it also has to be super durable and some amount of authentic. Like, if you're going for pure efficiency and inforation density you'd maybe engrave binary into acrylic, but what's the fun in that. There are so many remaining options from laser engraving slate to CNC routing clay tablets. Let's just research and try some?

### CNC engraving clay tablets

I started watching a bunch of youtube videos about low fire backyard pottery, where people make pottery on their own improvised coal furnaces and such. Some things I learned:

- get clay and mix in sand to make it less prone to exploding
- dry really really really well
- stick it in a bunch of coal and hope for the best

So I started making my clay tablet by mixing sand into the clay and adding water. Then I used (wet) planks to make the tablet as flat as possible.

![clay tablet](/images/lithopedia/wetclay.jpg)

Then I copied a wikipedia page into a text file and used [hf2gcode](https://github.com/Andy1978/hf2gcode) to make gcode:

```bash
text=$(python - $1 <<END
import unidecode, sys
with open(sys.argv[1]) as f:
    print(unidecode.unidecode(f.read()))
END
)
echo "$text" | fold -w 20 | ./src/hf2gcode -s 0.3 -n 12
```

After letting the clay dry and with my gcode in hand I went to Tkkrlab to try to CNC it. I used bCNC for this. The first try chipped off some bits, so I sprayed some water to make it more soft. But the text was way too small, so I redid the gcode with a bigger font and only the first paragraph of the wikipedia page.

![small font attempt](/images/lithopedia/toosmall.jpg)

With a bigger font size and regular water spraying I was making good progress, untill the bit started cutting deeper and deeper. Despite my best efforts the tablet wasn't flat enough, so I had to stop, readjust the CNC, and do some hacky maneuvers to start the job from that point. Success?! Some parts were barely touching, but it's something!

![the cnc'd tablet](/images/lithopedia/result.jpg)

So I went back home and put it in the oven to dry further. First at below 100, then at 150, and then cranking it all the way up. I guess I was too impatient because it exploded.

![explosion](/images/lithopedia/disaster.jpg)

### Lessons learned

* I need to be way more patient with drying (and possibly avoid making bubbles?)
* It needs to be way flatter (or I need to measure and compensate)
* It looks amazing
* Information density is super low

So I'm a bit torn, the first two items are probably solvable, and I really love how it looks, but at this font size it can barely fit a paragraph. With a beter process I can maybe reduce it a little bit, in particular the line height is quite excessive, but it's just not going to fit an entire wikipedia page.

Do I continue pushing this, or do I try something else? I think I might try what laser engraving slate looks like once the laser cutter at Tkkrlab is back in operation.