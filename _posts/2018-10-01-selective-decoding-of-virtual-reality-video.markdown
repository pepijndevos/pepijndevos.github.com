---
layout: post
title: Partial Decoding of 360&deg; HD Virtual Reality Video
categories:
- futhark
---

<iframe width="560" height="315" src="https://www.youtube.com/embed/l9n9WsZtOFE" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen> </iframe>

I'm doing some mental cleaning, putting some ideas out there that I had saved up for a master thesis, startup, or other ambition. Starting with this VR-related idea.

I got this idea from a post by John Carmack about [5k video decoding on VR headsets](https://developer.oculus.com/blog/behind-the-tech-with-john-carmack-5k-immersive-video/), where he talks about the challenges of 360&deg; HD video. Basically, it's a lot of data, and the user is only looking at about 1/6 of it. The problem with partial decoding is that conventional video codecs use key frames and motion prediction. John's solution is to slice up the video in tiles with extra many key frames and decode those, with an extra low-resolution backdrop for quick head motions.

I thought there must be better ways, so I made a new video codec to do efficient partial decoding. It's based on the 3D discrete cosine transform, that I [implemented on the GPU in Futhark](/2018/07/04/loefflers-discrete-cosine-transform-algorithm-in-futhark.html). It's the same thing used in JPEG, with the third dimension being time.

Think of it like this: If you'd put all the video frames behind each other, you basically get a cube of pixels. So similar to how you compress areas of the same color in JPEG, now you can compress volumes of the same color over time.

The way compression like this works is that you take blocks of 8x8(x8) pixels, and transform them to frequency domain. (the cosine transform is family of the Fourier transform) A property of the cosine transform is that most of the important information is at low frequencies, so you can basically set the high-frequency parts to zero. Then you do lossless compression, which is great at compressing long runs of zeros.

Well, that's how JPEG and 3D-DCT video compression works, which has been written about a lot. That's not a new thing. But what's really great about 3D-DCT compared to motion prediction is that you can decode and arbitrary 8x8x8 cube without any extra data. This makes it great for VR video, I think.

What's even more cool: The DC component of the DCT is the average of the whole cube, so *without any decoding*, you can take the DC component to get your low-resolution back-drop. This is also 1/8th the frame rate, so it may be desirable to partially decode the frame, which is totally possible. You just apply the 1D inverse DCT to the time dimension and take the DC components of the 2D frames from there.

After implementing a proof of concept in Futhark (for the DCT) and Python (for the IO and interface), I sent an email to John Carmack with the video above. His reply:

> There are at least three companies working full time on schemes for partial video decode in VR.  I have been in communication with Visbit and TiledMedia, and I know there are a couple others.  An algorithm isn’t going to be worth much of anything, but a functioning service, like they are trying to do, may have some kind of acquisition exit strategy, but it isn’t looking great for them right now.

> Long ago, I did some investigation of 3D DCT for video compression, and it wasn’t as competitive as I hoped – 2D motion prediction winds up being rather more flexible than the DCT basis functions, and video frames are actually aliased in time due to shutter exposures being a fraction of the time duration, so it isn’t as smooth as the spatial dimensions.

Though the main reason I shelved this idea is that there is not really a viable path to get this onto VR headsets.
A mobile video codec pretty much has to be implemented in hardware, but for such a niche market, it's hard to imagine a way to realize this hardware.
If there were a CPU manufacturer interested in licensing my 3D-DCT IP block into their products, I'd be more than happy to finish the thing.
