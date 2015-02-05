---
layout: post
title: Simulating the solar system
categories:
- math
- astronomy
- astrolog
---

<iframe width="420" height="315" src="https://www.youtube.com/embed/1J234YA3w4s" frameborder="0" allowfullscreen> </iframe>

I was reading an old book about astrology, which contained a section about how to calculate your horoscope. It used crude lookup tables for the planets, and it was only valid until 2000.

I had also wondered if two planets were still considered conjunct if they where above/below each other.

So I set out to compute my horoscope in 3D using real orbits instead of lookup tables. I read some things on Wikipedia, but nothing I could translate to running code.

Then I finally found a [NASA page](http://ssd.jpl.nasa.gov/?planet_pos) that contains all the orbital parameters and a PDF explaining the math.

It took me a long time to get it working for two reasons. The first was solving Kepler’s equation to obtain the eccentric anomaly.

They mix and match degrees and radians in confusing ways. While it might make sense to take the cosine of a value in degrees, it certainly does not make sense to `math.cos`.

I suggest any math code to convert degrees to radians at the edges, and deal with radians only. Degrees should only be used for input and display. They make sense to humans.

Once you have an approximation of Kepler’s formula solved for eccentric anomaly, it is good to verify that the original formula returns your mean anomaly.

{% highlight python %}
def solve_kepler(eccentricity, mean_anomaly):
    # for the approximate formulae in the present context, tol = 10e-6 degrees is sufficient
    tolerance = 10e-6
    # E0 = M + e sin M
    eccentric_anomaly = mean_anomaly + (eccentricity * math.sin(mean_anomaly))
    # and itterate the following equations with n = 0,1,2,... unitl |delta E| <= tol
    while True:
        # delta M = M - (En - e sin En)
        delta_mean_anomaly = mean_anomaly - (eccentric_anomaly - (eccentricity * math.sin(eccentric_anomaly)))
        # delta E = delta M / (1 - e cos En)
        delta_eccentric_anomaly = delta_mean_anomaly / (1 - (eccentricity * math.cos(eccentric_anomaly)))
        # En+1 = En + delta E
        eccentric_anomaly += delta_eccentric_anomaly

        if abs(delta_eccentric_anomaly) <= tolerance:
            return eccentric_anomaly
{% endhighlight %}

The second problem was rotating the planet’s ellipse into the Sun’s coordinate system. Written out in Python it’s a screen full of trigonometric functions. Missing anything gives completely bogus results. It’s not hard, just tedious and error prone.

{% highlight python %}
def ecliptic_coordinates(orbit_x, orbit_y, perihelion, longitude_ascending, inclination):
    term1 = math.cos(perihelion) * math.cos(longitude_ascending)
    term2 = math.sin(perihelion) * math.sin(longitude_ascending) * math.cos(inclination)
    term3 = math.sin(perihelion) * math.cos(longitude_ascending)
    term4 = math.cos(perihelion) * math.sin(longitude_ascending) * math.cos(inclination)
    x = (term1 - term2) * orbit_x + (-term3 - term4) * orbit_y

    term1 = math.cos(perihelion) * math.sin(longitude_ascending)
    term2 = math.sin(perihelion) * math.cos(longitude_ascending) * math.cos(inclination)
    term3 = math.sin(perihelion) * math.sin(longitude_ascending)
    term4 = math.cos(perihelion) * math.cos(longitude_ascending) * math.cos(inclination)
    y = (term1 + term2) * orbit_x + (-term3 + term4) * orbit_y

    term1 = math.sin(perihelion) * math.sin(inclination)
    term2 = math.cos(perihelion) * math.sin(inclination)
    z = term1 * orbit_x + term2 * orbit_y

    return x, y, z
{% endhighlight %}

And in the end I did not even use this code. Once I started using Processing, I also wanted to draw the orbits themselves. This should not be hard, given the semi-major axis and eccentricity.

I again hit two problems. The first is that Processing’s `ellipse` function takes a center point, height and width. But I solved this using [Wikipedia](http://en.wikipedia.org/wiki/Ellipse#Eccentricity) and math I can understand.

{% highlight python %}
def planet_ellipse(planet, jd):
    eccentricity = planet.eccentricity(jd)
    smajor = planet.semi_major_axis(jd)
    sminor = smajor * math.sqrt(1 - (eccentricity ** 2))
    center = smajor * eccentricity
    return smajor * 2, sminor * 2, center
{% endhighlight %}

The tougher problem is rotating the ellipse to match the planet. While I successfully translated the math to code, I had no idea what is going on. I still have no clue how this pile of trigonometry works.

Instead I substituted the function arguments to `ecliptic_coordinates` for `mouseX` and `mouseY` to experiment what is going on. After some failed experiments I found some rotation commands that would align the ellipse with the planet. Then I removed the trigonometric mess and simply drew the planet in the same plane as the ellipse.

{% highlight python %}
def planet_rotation(planet, jd):
    perihelion = planet.longitude_perhelion(jd) - planet.longitude_ascending(jd)
    rotateZ(-planet.longitude_ascending(jd))
    rotateX(planet.inclination(jd))
    rotateZ(-perihelion)
{% endhighlight %}

That’s it. The rest is mostly just drawing code. You can run [the code](https://github.com/pepijndevos/planetarium) for yourself.

I really like how it turned out. If you pan around you can clearly see that Pluto might not be as conjunct as it looks in 2D. If you let things rotate, you can visually see Mercury speed up near the sun. It’s also intuitively clear how retrograde motion works.

The only thing missing is the moon. I could not find orbital parameters for it.

