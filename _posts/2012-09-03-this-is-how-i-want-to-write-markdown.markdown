---
layout: post
title: This is How I Want to Write Markdown
categories:
- html
- js
- markdown
---

Most websites us a split-pane approach to Markdown editing. You enter your text in one field, and the resulting HTML appears in another.

<img src="/images/mac_feature_07.jpg" style="float:right" />
I think this is silly UI, and would like to see websites adopt the [iA Writer][1] approach to Markdown editing.

Sadly, I don't know of any library or parser that lets you do this, so I started writing one.

This thing is currently easier to break than to use, partially because my approach is broken, partially because the content editable API is broken, and partially because it's just a one-day hack.

<div id="editor" style="outline:1px solid black; min-height:200px;"> </div>

<div> <script src="http://pepijndevos.github.com/markin/markin.js"> </script> <script> edit("editor"); </script> <link rel="stylesheet" href="http://pepijndevos.github.com/markin/remarkdown.css" /> </div>

[1]: http://www.iawriter.com