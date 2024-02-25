---
layout: post
title: "Library 2000 part 2: Tablets for Sale"
categories:
- lib2k
- cnc
---

I'm making a clay tablet library with media from around the 20th century, to preserve our culture for the ages and answer the question: What do we want the distant future to know about us?

A lot has happened since the [first part](/2023/05/20/lithopedia-part-1-intro-and-clay-experiments.html):

- I renamed the project after learning about [lithopedion](https://en.wikipedia.org/wiki/Lithopedion)
- I had a private lesson from [ceramic artist Ilse Scholten](https://www.ilsescholten.nl/)
- I rewrote my text to gcode scripts
- I made a bunch of tablets and had them fired
- I started working towards a website for the project

Let's start from the result and work back through the details. I made a bunch of clay tablets, had them fired by Ilse, and put them up **for sale on [my Etsy store](https://www.etsy.com/shop/Library2000)**. Here is a photo album to check out:

<a data-flickr-embed="true" href="https://www.flickr.com/photos/pepijndevos/albums/72177720315047612" title="Library 2000"><img src="https://live.staticflickr.com/65535/53551511959_c7733cc50c_w.jpg" width="500" height="375" alt="Library 2000"/></a><script async src="//embedr.flickr.com/assets/client-code.js" charset="utf-8"></script>

The reason it took so long to get the second update out is that I wanted to build a proper website with the concept, motivation, tutorials, a gallery, and a webshop. But as a suprpise to no one that's quite a big project on its own, so I finally decided to write another update on my blog meanwhile.

The first key element to the new proccess is my newfound gcode swiss army knife: vpype. With [vpype-gcode](https://github.com/plottertools/vpype-gcode) turning an SVG or text file into gcode is a one-liner, you just need a config file for your CNC and a few command-line flags.

```bash
vpype --config vpype.toml pagesize a5 text --position 1cm 1cm --wrap 12.5cm --size 22pt --hyphenate en --justify "$RANDOM_PAGE_TEXT" linemerge show gwrite --profile cnc random.gcode
```

But the biggest change was in the preparation of the clay slab itself. I abandoned my failing attempts at making a mold, and followed Ilse's advice to roll the clay between strips of wood.

### Tutorial

Materials needed:
- clay (0.2mm chamotte)
- clay wire cutter
- clay rib
- dough roller
- plasterboard
- hardwood strips (5mm to 10mm thick)
- some old bedsheet
- knife

To make a clay tablet you obviously need clay. It is important that the clay contains chamotte, which makes it less likely to explode in the oven. It's also nice if the chamotte is small to get smooth writing. Clay with 0.2mm chamotte has worked the best for me.

We need a very flat surface, we're aiming for sub milimeter precision to get consistent writing.
Plasterboard is the surface that Ilse recommended, which has worked well for me. (unlike wood which will warp)
I cut off roughly square sheets and taped the edges so we can safely move the drying tablets around.

It is very important to work on top of a smooth non-strechy cloth. I use a cut up cotton bedsheet.
This allows us to flip the tablet over, inspect it for air bubbles, and prevents it from sticking to the plasterboard and warping while drying.

![working surface](/images/lib2k/IMG_20240224_160928_273.jpg)

Now we can get to work. Cut off a piece of clay with a wire cutter, place it on the cloth betweent the wood strips, and use the dough roller to flatten it out. If you're reusing scraps from a pervious tablet, first knead the clay into a ball, while making sure not to knead any air bubbles.

![roll the clay](/images/lib2k/IMG_20240224_161108_407.jpg)

After rolling it flat we can cut off the excess clay with a knife. I use a folded piece of paper as a reference. Then take the rib and smooth out the surface, making it nice and shiny.

An important step at this point is to slightly lift the fabric, bending the tablet. If there are any air bubbles they will show up as little blisters. Poke them with a knife and flatten the tablet again.

Now cover the tablet with a cloth and flip it over, taking care not to leave fingerprints. Then take the rib and smooth the other side. Then flip it to whichever side looks nicer to become the front.

![finish the tablet](/images/lib2k/IMG_20240224_161627_909.jpg)

With my current process I put the entire board with the wet clay tablet right under the CNC.
I give it a final roll with the dough roller because clay has a little bit of memory.
Then I zero the CNC on the wood strip to get the correct height without leaving a mark.
For the CNC bit I actually use a thick needle in a 2mm chuck at 0 RPM, I found that the stationary sharp point produces the finest writing in the wet clay.

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/I9oRr1zIfXg?si=4fJ2ZQkMWpgtY0MY" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>

A sneak peak at my current experiments is that I'm trying out what happens if you let the clay dry a little bit. Maybe there is a sweet spot where you can CNC it without accumulating or chipping, but I've not yet found it. I'm also experimenting with V-carving to be able to engrave more complicated graphics such as mathematical equations. My current goal is to engrave Maxwell equations on a tablet, so that's a whole new can of worms.