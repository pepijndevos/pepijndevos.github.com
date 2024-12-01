---
layout: post
title: JPEG compress your LLM weights
categories:
- machinelearning
---

So quantization is kinda bad lossy compression right? JPEG is good lossy compression.
This may sound stupid, and maybe it is, but hear me out.

I've read that LLM performance is usually constrained by memory bandwidth, and for us plebs also by memory size, and there is a precedent in for example [ZFS compression](https://www.zfshandbook.com/docs/advanced-zfs/compression/) which has shown to _increase_ disk performance because you're IO constrained rather than compute constrained.
So it might be beneficial to decompress LLM parameters on the fly, and if you're doing that you might want to use a good lossy compression algorithm instead of blunt quantization.
[It is said that compression is equivalent to general inteligence](/2023/07/15/chatlmza.html), so in that sense lossy compression would be expected to reduce inteligence, so you'd want to get a good compression ratio with minimal loss.

The way JPEG works is basically
- break down the pixels in chunks - after decompression chunk boundaries are visible as JPEG artifacts.
- Discrete Cosine Transform them - lossless transformation in the family of Fourier transforms
- quantize them - data loss happens here, creating longer runs
- Run Length Encode them - compression happens here

RLE is a lossless compression technique, which gets turbocharged by discarding some data to create longer runs.
In the case of image data, the DCT concentrates most information in the low frequencies so you can quantize high frequencies with minor loss in image quality.
Now, I don't expect LLM parameters to be "smooth" like image data, so naive JPEG compression of LLM weights is not likely to be effective.

BUT!

You can reorder the collumns and rows of a matrix without affecting the result. It's like \\(a+b+c=d \rightarrow c+b+a=d\\).
So you could reorder your rows and columns to maximize clustering of similar values.
Not sure how you'd do this, maybe just sort by vector sum, or some genetic algorithm, or [other cleverness](https://www.mathworks.com/help/matlab/math/sparse-matrix-reordering.html).

So my proposed LLM compression would work like this
- reorder the matrices to improve value clustering
- break down the values in chunks
- DCT them
- quantize them
- RLE them

And then inference would
- RLE expand a chunk
- inverse DCT it
- perform the multiplications

So the compressed data would exist in VRAM and be decompressed on the fly chunk by chunk to perform a matrix vector product.
It'd take more compute, [11 multiplications to be precise](/2018/07/04/loefflers-discrete-cosine-transform-algorithm-in-futhark.html), but if you're memory constrained it could be worth it.

I guess the real question is if you can obtain any useful clustering in LLM data.
In a sense the parameters are already compressed(=intelligence), but there is no information in their order, so reordering and transforming parameters could improve RLE compression without incurring extra quantization loss.