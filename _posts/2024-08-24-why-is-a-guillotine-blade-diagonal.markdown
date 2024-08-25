---
layout: post
title: Why is a Guillotine blade diagonal?
categories:
- history
- mathematics
---
[this is a public draft]

This is a theoretical counterpart to an experimental collaboration between [KnowArt](https://www.youtube.com/@KnowArt) and [Proper Printing](https://www.youtube.com/@properprinting) to see if a diagonal guillotine blade cuts better than a horizontal one.

[insert video's here]

A raised guillotine blade has a certain potential energy which is transferred to the contact point between the blade and the neck. So let's assume a frictionless spherical cow, I mean neck, and calculate the contact point, force, and energy as a function of the angle of the blade.

The equation of a circle is given by \\(x^2+y^2=r^2\\), so a horizontal blade intersecting the circle of radius \\(r\\) at height \\(y\\) intersects the circle at \\(x=\pm\sqrt{r^2+y^2}\\). The length of the cut is therefore \\(L(y)=2\sqrt{r^2+y^2}\\).

For a diagonal blade, we can rotate the reference frame to be aligned to the blade, such that the knife has a horizontal component of \\(y\sin(\theta)\\) and a vertical component of \\(y\cos(\theta)\\), creating a contact patch of \\(L(y\cos(\theta))=2\sqrt{r^2+y^2\cos^2(\theta)}\\).

![rotated guillotine](/images/guillotine/geometry.svg)

So now we can express the force from the contact area in terms of some constant \\(k\\) as \\(F(y)=kL(y)\\) perpendicular to the blade edge, resulting in a vertial and horizontal component:

$$\begin{aligned}
F_x(y)&=\sin(\theta)kL(y)\\
F_x(y)&=\sin(\theta)k2\sqrt{r^2+y^2\cos^2(\theta)}\\
F_y(y)&=\cos(\theta)kL(y)\\
F_y(y)&=\cos(\theta)k2\sqrt{r^2+y^2\cos^2(\theta)}
\end{aligned}$$

This means that an angled blade creates "leverage" where less force but a longer travel is required. To be precise, the travel is given by \\(\frac{2r}{\cos(\theta)}\\).

![vertical component of cutting force](/images/guillotine/vertical_cut_force_plot.svg)

To compute the total energy required for a cut, assuming the dominant force scales with the length of the contact patch, is the integral of the force over the vertical travel component.

$$W_y = k \cos(\theta) \int_{-\frac{r}{\cos(\theta)}}^{\frac{r}{\cos(\theta)}} L(y\cos(\theta)) dy$$

Now we can simplify by substituting \\(u=y\cos(\theta)\\), \\(dy=\frac{du}{\cos(\theta)}\\) and adjust the integration bounds accordingly, cancelling all \\(\theta\\) terms!

$$W_y = k \int_{-r}^{r} L(u) du$$

So just as much vertical energy is required to cut through the circle no matter the angle, but we're also excerting a horizontal force. If the guillotine blade is running on math bearings this is of no concern, but for a wooden sled there is a (Coulomb) friction of \\(F_c=\mu F_x\\) giving

$$\begin{aligned}
W_x &= \mu k \sin(\theta) \int_{-\frac{r}{\cos(\theta)}}^{\frac{r}{\cos(\theta)}} L(y\cos(\theta)) dy\\
W_x &= \mu k \tan(\theta)\int_{-r}^{r} L(u) du
\end{aligned}$$

And indeed KnowArt experimentally found that the diagonal blade performed worse due to being pushed sideways.

The one thing the diagonal blade has going for it is the one thing that is not easily captured in mathematics or tatami rolls: A diagonal blade as a horizontal component that creates a slicing motion rather than a chopping motion. And anyone who's done any amount of cooking knows from experience that this can help a lot.

But the story goes that the real reason the blade is diagonal is that the king suggested it might help with people with fat necks. Ironically his own fat neck ended on the block some time later.