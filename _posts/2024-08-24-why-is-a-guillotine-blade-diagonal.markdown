---
layout: post
title:  Why Are Guillotine Blades Angled? (analyzed) 
categories:
- history
- mathematics
---
This is a theoretical counterpart to an experimental collaboration between [KnowArt](https://www.youtube.com/@KnowArt) and [Proper Printing](https://www.youtube.com/@properprinting) to see if a diagonal guillotine blade cuts better than a horizontal one.

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/53fSsfUjUeI?si=lC3MDF0hAtdnQhn6" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>

A raised guillotine blade has a certain potential energy which is transferred to the contact point between the blade and the neck. So let's assume a frictionless spherical cow, I mean neck, and calculate the contact point, force, and energy as a function of the angle of the blade.

The equation of a circle is given by \\(x^2-y^2=r^2\\), so a horizontal blade intersecting the circle of radius \\(r\\) at height \\(y\\) intersects the circle at \\(x=\pm\sqrt{r^2-y^2}\\). The length of the cut is therefore \\(L(y)=2\sqrt{r^2-y^2}\\).

For a diagonal blade, we can rotate the reference frame to be aligned to the blade, such that the knife has a horizontal component of \\(y\sin(\theta)\\) and a vertical component of \\(y\cos(\theta)\\), creating a contact patch of \\(L(y\cos(\theta))=2\sqrt{r^2-y^2\cos^2(\theta)}\\).

![rotated guillotine](/images/guillotine/geometry.gif)

So now we can express the force from the contact area in terms of some constant \\(k\\) as \\(F(y)=kL(y)\\) perpendicular to the blade edge, resulting in a vertial and horizontal component:

$$\begin{aligned}
F_x(y)&=\sin(\theta)kL(y)\\
F_x(y)&=\sin(\theta)k2\sqrt{r^2-y^2\cos^2(\theta)}\\
F_y(y)&=\cos(\theta)kL(y)\\
F_y(y)&=\cos(\theta)k2\sqrt{r^2-y^2\cos^2(\theta)}
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

But what about the pointy blade? I'm not glad you asked because the math is more messy. We can consider one half of the pointy blade at a rotated reference frame. So we get the same \\(L\\) as before, but now the blade stops at the tangent line of the blade angle, as follows. But then once the point of the blade exits the circle it becomes two flat sections, forming a piecewise function.

![pointy blade geometry](/images/guillotine/desmos-graph.svg)

For the first part we have half of a flat blade length \\(L\\), the uncut section \\(M\\), and the pointy blade section \\(N\\)

$$\begin{aligned}
L&=\sqrt{r^2-(\cos(\theta)h)^2} \\
M&=\tan(\theta)\cos(\theta)h\\
&=\sin(\theta)h \\
N&=L-M \\
&=\sqrt{r^2-(\cos(\theta)h)^2}-\sin(\theta)h
\end{aligned}$$

Giving us

$$\begin{aligned}
L_{tot} &=\begin{cases} 
      2\left(\sqrt{r^2-\cos^2(\theta)h^2}-\sin(\theta)h\right) & -r < h \leq r \\
      4\sqrt{r^2-\cos^2(\theta)h^2} & r < h \leq \frac{h}{\cos(\theta)} \\
   \end{cases} \\
F_y &= \cos(\theta)k L_{tot}
\end{aligned}$$

![pointy force](/images/guillotine/pointy_blade_cut_force.svg)

For the total work, we already know the square root terms are independent of the angle. And since the sum of the integral is the same as the integral of the sum, we only need to concern ourselves with the \\(\sin(\theta)h\\) term.

$$\begin{aligned}
W_p &= k\cos(\theta)\sin(\theta)\int_{-r}^r h dh \\
W_p &= k\cos(\theta)\sin(\theta)\left(\frac{1}{2}(-r)^2-\frac{1}{2}(r)^2\right) \\
W_p &= 0
\end{aligned}$$

So all our blades take the same amount of work to cut through an uniform saussage, with the diagonal blades being worse due to friction. The brachistochrone blade is left as an exercise to the reader.

But there is more to cutting than uniform math saussages. KnowArt indeed found that the diagonal blade seemed to perform worse than the flat blade due to friction. But the pointy blade performed better. The most likely explanation for this is that a horizontal slicing motion is beneficial for cutting. This makes intuitive sense to anyone who's done some cooking, and was also explained to me by a medical doctor relative as the difference between a sword and a sabre.

But the story goes that the real reason the blade is diagonal is that the king suggested it might help with people with fat necks. Ironically his own fat neck ended on the block some time later.