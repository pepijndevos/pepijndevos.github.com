---
layout: post
title: Loefflers Discrete Cosine Transform algorithm in Futhark
categories:
- futhark
---

If you search for Loefflers algorithm you get a few academic papers, and for Futhark you get the Germanic runes. This post is a SEO masterpiece.

Discrete Cosine Transform is a variation on the Discrete Fourier Transform. It is used in basically every lossy compression format ever. The reason DCT is preferred is that discrete transforms are cyclic. So the DFT has a jump at the edges of the data, where it wraps around. (this is why windowing is frequently used in DFT) This jump at the edges leads to a fat tail in the frequency spectrum, which does not compress well.

The DCT constructs an "even" signal (mirrored around the 0 axis), so the signal is continuous at the edges. This leads to much lower high frequency coefficients. Lossy compression basically works by quantizing/masking/thresholding those coefficients, which produces many zeros at high frequencies. Long runs of zeros compress really well, so that's what happens in most compression algorithms.

I was playing a bit with compression, but found that `scipy.fftpack.dct` was not fast enough to my liking. Since I had recently discovered [Futhark](https://futhark-lang.org/), which is an amazing ML-like functional programming language for GPU programming that compiles to OpenCL, I thought it'd be fun to implement the DCT in Futhark. Little did I know what I was getting myself into.

After some searching, I found that Loefflers algorithm is the way to go. It's what *everyone* seems to be using, because for an 8-point DCT it obtains the theoretical lower bound of 11 multiplications. After chasing some references in more recent papers, I found the original: [Practical fast 1-D DCT algorithms with 11 multiplications](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.463.3353&rep=rep1&type=pdf), and after days of struggling, I almost understood it.

I knew that a Fast Fourier Transform is based on taking the DFT equation, and splitting it up in odd and even parts. If you keep doing this recursively (called decimation in time/decimation in frequency), you end up with this "butterfly" structure, which are additions of two "branches" scaled by some factor. For the DCT there are also butterflies, but also rotation blocks.

It took a few mental leaps to understand that you can write the DCT or DFT in matrix form, express elementary row operations in matrix form, use those to factorize the DCT matrix, and derive an optimal implementation from this matrix factorization.

The Futhark side of things was really fun. If you know a bit of functional programming, it's really not hard, and you don't need to know anything about OpenCL or GPU's. I hopped on Gitter, and Troels Henriksen was super helpful. I'd come up with a problem, and a few hours later I'd `git pull` and the compiler got better.

There were a few surprises though. Many array operations are basically free, by returning a view of the same array. But there is an underlying assumption that arrays are big, heap allocated, and parallelized relentlessly. Tuples, on the other hand, are assumed to be small, and register allocated. By rewriting my inner DCT structure from (tiny) arrays to using tuples, performance more than doubled.

At first I tied to optimize my code to use in-place updates, but this was actually significantly *slower* than out-of-place. By doing in-place updates, I force the compiler to do the operations completely sequentially, while normally it could do a lot in parallel. It turns out that moving data around is by far the slowest thing, and arithmetic is basically free. So the best way to write fast code is to move less data, not to worry about every addition.

Actually implementing the DCT was really hard though. As I mentioned, searching for it brought up only academic papers, barely any working code. I managed to find two eventually: [dct_simd](https://github.com/norishigefukushima/dct_simd/blob/master/dct/dct8x8_simd.cpp) and [mozjpeg](https://github.com/mozilla/mozjpeg). I actually ported the first one to Python to compare intermediate results with my own implementation.

{% highlight python %}
def pydct(x):
    r = [1.414214, 1.387040, 1.306563, 1.175876, 1.000000, 0.785695, 0.541196, 0.275899]
    y = np.zeros(8)

    invsqrt2= 0.707107;

    c1 = x[0]; c2 = x[7]; t0 = c1 + c2; t7 = c1 - c2;
    c1 = x[1]; c2 = x[6]; t1 = c1 + c2; t6 = c1 - c2;
    c1 = x[2]; c2 = x[5]; t2 = c1 + c2; t5 = c1 - c2;
    c1 = x[3]; c2 = x[4]; t3 = c1 + c2; t4 = c1 - c2;

    c0 = t0 + t3; c3 = t0 - t3;
    c1 = t1 + t2; c2 = t1 - t2;

    y[0] = c0 + c1;
    y[4] = c0 - c1;
    y[2] = c2 * r[6] + c3 * r[2];
    y[6] = c3 * r[6] - c2 * r[2];

    c3 = t4 * r[3] + t7 * r[5];
    c0 = t7 * r[3] - t4 * r[5];
    c2 = t5 * r[1] + t6 * r[7];
    c1 = t6 * r[1] - t5 * r[7];

    y[5] = c3 - c1; y[3] = c0 - c2;
    c0 = (c0 + c2) * invsqrt2;
    c3 = (c3 + c1) * invsqrt2;
    y[1] = c0 + c3; y[7] = c0 - c3;
    return y

def pyidct(y):
    r = [1.414214, 1.387040, 1.306563, 1.175876, 1.000000, 0.785695, 0.541196, 0.275899]
    x = np.zeros(8)
    
    z0 = y[1] + y[7]; z1 = y[3] + y[5]; z2 = y[3] + y[7]; z3 = y[1] + y[5];
    z4 = (z0 + z1) * r[3];

    z0 = z0 * (-r[3] + r[7]);
    z1 = z1 * (-r[3] - r[1]);
    z2 = z2 * (-r[3] - r[5]) + z4;
    z3 = z3 * (-r[3] + r[5]) + z4;

    b3 = y[7] * (-r[1] + r[3] + r[5] - r[7]) + z0 + z2;
    b2 = y[5] * ( r[1] + r[3] - r[5] + r[7]) + z1 + z3;
    b1 = y[3] * ( r[1] + r[3] + r[5] - r[7]) + z1 + z2;
    b0 = y[1] * ( r[1] + r[3] - r[5] - r[7]) + z0 + z3;
    #return np.array([z0, z1, z2, z3])

    z4 = (y[2] + y[6]) * r[6];
    z0 = y[0] + y[4]; z1 = y[0] - y[4];
    z2 = z4 - y[6] * (r[2] + r[6]);
    z3 = z4 + y[2] * (r[2] - r[6]);
    a0 = z0 + z3; a3 = z0 - z3;
    a1 = z1 + z2; a2 = z1 - z2;
    #return np.array([a0, a1, a2, a3, b0, b1, b2, b3])

    x[0] = a0 + b0; x[7] = a0 - b0;
    x[1] = a1 + b1; x[6] = a1 - b1;
    x[2] = a2 + b2; x[5] = a2 - b2;
    x[3] = a3 + b3; x[4] = a3 - b3;
    return x
{% endhighlight %}

From this code and staring at the paper, I learned a few things. First of all **figure 1 is wrong**. The rotate block should be `sqrt(2)c6` instead of `sqrt(2)c1`. Another small detail is the dashed lines, meaning that **some butterflies are upside down**. Another one is the rotate block symbol. It says `kcn`, which are the `k` and `n` in the equation, **not** the one in the DCT equation, which confused me a lot. So for `sqrt(2)c6` you just substitute `sqrt(2)` and 6 in the rotation block. I noted down some more insights in response to a [two year old question about the paper on the DSP StackExchange](https://dsp.stackexchange.com/questions/28209/fast-dct-implementation/50223#50223)

Having implemented the forward DCT from the paper, I moved on to the inverse. All information the paper has about this is "just do everything backwards". Thanks, paper. It turns out you use the **same** blocks, but in the reverse order, except... the rotate block `n` becomes `-n`. The inverse cosine transform has a negative angle, and this translates to `cos(-x)=cos(x)`, `sin(-x)=-sin(x)`.

{% highlight text %}
type octet 't = [8]t
type f32octet = octet f32

let butterfly (a: f32) (b: f32) : (f32, f32) =
  (a+b, a-b)

let mk_coef (k:f32) (n:i32) : f32 =
  k*f32.cos ((r32 n)*f32.pi/16)

let coefr = map (mk_coef (f32.sqrt 2)) <| iota 8
let coef1 = map (mk_coef 1) <| iota 8

let rotate (sin_coef: f32) (cos_coef: f32) (a: f32) (b: f32): (f32, f32) =
  (a*cos_coef + b*sin_coef,
   b*cos_coef - a*sin_coef)

entry fdct8 (a: f32octet) : f32octet  =
  -- stage 1
  let (st1_0, st1_7) = butterfly a[0] a[7]
  let (st1_1, st1_6) = butterfly a[1] a[6]
  let (st1_2, st1_5) = butterfly a[2] a[5]
  let (st1_3, st1_4) = butterfly a[3] a[4]
  -- even part, stage 2
  let (st2_0, st2_3) = butterfly st1_0 st1_3
  let (st2_1, st2_2) = butterfly st1_1 st1_2
  -- stage 3
  let (y0, y4)   = butterfly st2_0 st2_1
  let (y2, y6)   = rotate coefr[2] coefr[6] st2_2 st2_3
  -- odd part, stage 2
  let (st2_4, st2_7)   = rotate coef1[5] coef1[3] st1_4 st1_7
  let (st2_5, st2_6)   = rotate coef1[7] coef1[1] st1_5 st1_6
  -- stage 3
  let (st3_4, st3_6)   = butterfly st2_4 st2_6
  let (st3_7, st3_5)   = butterfly st2_7 st2_5
  -- stage 4
  let (y1, y7)   = butterfly st3_7 st3_4
  let y3  = f32.sqrt(2)*st3_5
  let y5  = f32.sqrt(2)*st3_6
  in [y0, y4, y2, y6, y7, y3, y5, y1]


entry idct8 (a: f32octet) : f32octet  =
  -- odd part, stage 4
  let (st4_7, st4_4)   = butterfly a[7] a[4]
  let st4_5 = f32.sqrt(2)*a[5]
  let st4_6 = f32.sqrt(2)*a[6]
  -- stage 3
  let (st3_4, st3_6)   = butterfly st4_4 st4_6
  let (st3_7, st3_5)   = butterfly st4_7 st4_5
  -- stage 2
  let (st2_4, st2_7)   = rotate (-coef1[5]) coef1[3] st3_4 st3_7
  let (st2_5, st2_6)   = rotate (-coef1[7]) coef1[1] st3_5 st3_6
  -- even part, stage 3
  let (st3_0, st3_1)   = butterfly a[0] a[1]
  let (st3_2, st3_3)   = rotate (-coefr[2]) coefr[6] a[2] a[3]
  -- stage 2
  let (st2_0, st2_3) = butterfly st3_0 st3_3
  let (st2_1, st2_2) = butterfly st3_1 st3_2
  -- stage 1
  let (st1_0, st1_7) = butterfly st2_0 st2_7
  let (st1_1, st1_6) = butterfly st2_1 st2_6
  let (st1_2, st1_5) = butterfly st2_2 st2_5
  let (st1_3, st1_4) = butterfly st2_3 st2_4
  in [st1_0/8, st1_1/8, st1_2/8, st1_3/8, st1_4/8, st1_5/8, st1_6/8, st1_7/8]
{% endhighlight %}
