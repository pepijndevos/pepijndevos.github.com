---
layout: post
title: CSS tips and tricks
categories:
- css
- sombrero
---

While working on [Sombrero][1], I encountered a lot of things I did not know how to do properly. I hope they help someone.

## width: cling;

Block elements normally fill up all the available width, except when you apply `position` or `float` to them. I needed some element to take up the *minimal* width, without the side effects of floating or positioning. `display: table;` is the solution I was looking for.

## Full-width form fields

Another problem in my magic alignment layout where full-width form fields. Input elements do not take space like normal block elements, no matter the `display` property.

`width: 100%;` works, but only as long as you have absolutely no borders, margin, or padding. To get around that, CSS 3 has a property to use the IE 5 box model, which includes padding and borders in the width: `box-sizing: border-box;`

## Collapsing margins

I can't imagine why I never noticed before, but it turns out CSS collapses margins on "regular" block elements. [It's complicated][2].

## display: run-in;

I never used it, but it seems like an interesting property. It makes things like headings appear inline with the content of the block below it.

## Styling range input fields

Some modern browsers support input fields of `type="range"`, for imprecise number values. However, style on these elements is normally completely ignored.

Bring in `-webkit-appearance: none;`, which turns the slider track back into a normal box. Then apply the knob styling to the `::-webkit-slider-thumb` pseudo element.

## position all four corners

Another part in my alignment puzzle. Note that not all browsers support this. If you can't do with 2 sides plus size, it is possible to specify all 4 and leave the sizing implicit.

[1]: https://github.com/pepijndevos/Sombrero
[2]: http://reference.sitepoint.com/css/collapsingmargins