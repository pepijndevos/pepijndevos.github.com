---
layout: post
title: Lat/Lon to Meter
categories:
- math
---

Easy, 1 Latitude is the circumference of the earth divided by 360: 111 km

1 Longitude depends on your Latitude, so… copy-paste

    math.radians(6378137.0 * math.cos(math.radians(lat)))

Now, I know this all breaks down horribly when you consider large distances or require high precision(it also upsets mathematicians), but I don't care(much).

All I[^1] am interested in is, if I stand at 42° Latitude and I walk North 10 meter, about what is my new Latitude?

[^1]: And probably half the googlers and questioners on Stack Overflow.