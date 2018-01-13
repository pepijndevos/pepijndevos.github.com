---
layout: post
title: "Generate Cities: Skylines height maps"
image: /images/heightmap.gif
categories:
- game
- gis
---

Over the Christmas holiday I played a bit of Cities: Skylines. I wondered if it would be possible to import a real-world location from Google Maps or Open Street Map or something. I quickly found [terrain.party](http://terrain.party), and downloaded a couple of locations.

I found some [videos](https://www.youtube.com/watch?v=L-kutzfaI8g) of people building polders, so I thought it'd be fun to import a real Dutch polder to play on. But to my disappointment, the height map from [terrain.party](http://terrain.party) was quite bad around the coastlines and did not seem to deal well with land below the sea level.

However, the readme on the files explained the data sources and the file format used by Cities: Skylines. So it seemed pretty easy to write a simple script to generate my own tiles. Nothing is ever as easy as it seems, but a week later, I have a somewhat working script that generates playable maps that look nicer than [terrain.party](http://terrain.party).

![comparison](/images/heightmap.gif)

My script is based on exactly the same SRTM3 data als used by [terrain.party](http://terrain.party) (amongst other sources), but produces much nicer shorelines as well as smoothing the raw data to avoid that pixelated look. There are also a lot of other parameters to tweak.

After some searching, I found the [Mapzen's S3 bucket](https://aws.amazon.com/public-datasets/terrain/) which offers easy access to the void-filled, 30m resolution SRTM3. But the real key to smooth shorelines is the [SRTM Water Body Data](https://dds.cr.usgs.gov/srtm/version2_1/SWBD/SWBD_Documentation/Readme_SRTM_Water_Body_Data.pdf), which I used to mask out the water with a negative altitude. As a finishing touch I applied a light Gaussian blur to the upscaled data to remove the hard edges.

[Download the script here](https://github.com/pepijndevos/citiesheightmap)

Initially I developed the script under Linux, but switching to Windows for testing with the actual game was surprisingly painless, once I installed Anaconda. Anaconda takes care of compiling all the annoying C extensions, which is a huge relief. I highly recommend it.

For my personal use, some parameters and assumptions were just hardcoded, but I've since added a simple command-line interface so yo can adjust the size of the map, how much blur is applied, height scale and offset, and disable shoreline fixes.
