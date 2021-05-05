---
layout: post
title: "Evaluating GUI toolkits: React, Elm, Yew, ClojureScript, Flutter"
categories:
- mosaic
---

For Mosaic, my open source schematic entry and simulation tool, I'm reevaluating the choice of GUI toolkit, and along with that the implementation language.
The original plan was to use Rust and GTK, but I'm slowly comming around to the idea that if its feasible performance wise to make a webapp, that there is great value in that.

On the one hand IC designers are a conservative bunch and it's great to have a good desktop app, but on the other hand EDA tools can be notoriously hard to set up and manage to the point where most commercial IC design happens via remote desktop.
Having a the option to a webapp allows you to have a snappy interface without all the hassle, and as eFabless demonstrated allows people to do IC design without signing an NDA.

I kind of want the option to run as a desktop app or as a web app. So what are my options?
I think the most common one is to use Electron, which everyone likes to hate on, but in fact I'm typing up this blogpost in an Electron app and I can't complain.
Another option would be Flutter, which has web and desktop targets.
And finally, some desktop GUI toolkits can be ran in a browser, which I'll almost dismiss right away because they don't behave like a proper web app.

My plan is to make some very simple proof of concepts in various GUI toolkits to see how they stack up. I plan to look at

 * React + TypeScript
 * Elm
 * Yew (Rust wasm)
 * ClojureScript?
 * Flutter (Not Electron!)

I will consider the following criteria

 * Ease of use
 * Performance
 * Not breaking basic shit
 * Churn

By implementing the following tasks that I deem critical for my use as a schematic entry and simulation tool

 * Schematic: Draw a bunch of SVGs and lines that can be panned and zoomed.
 * Scripting: Interface with an IPython kernel for a minial notebook style interface.
 * Plotting: Plot a simulation result with a lot of traces and time points that can be panned and zoomed.

### React + TypeScript

Basically I blinked and the entire ecosystem changed. Are Grunt and Gulp not a thing anymore? Is Yarn the new npm?
I would very much like to not rewrite my whole app every year.
But hey, React and TypeScript seem to be still around and going strong so that's something.

I tried to figure out how to set this up and kinda just got lost in boilerplate.
I was about to just go back to Makefiles and script tags when someone pointed out Next.js which allows you to create an app with a simple `yarn create next-app`.
I'm sure it has a bunch of things I don't need but at least it gets me a good setup with little hassle.


### Flutter

On the surface it seems like the dream combo.
Instead of a bloated Electron app you get a snappy desktop app that can also render to the web.
It's not all roses though, it's originally a mobile toolkit, and it kind of shows.
The desktop target it still in beta, missing advanced features such as "file picker" and "menu bars", who needs 'em right?

Flutter has a nice [gallery](https://gallery.flutter.dev/) where you can browse around some example apps.
I opened the email app and **you can't select the text in an email**. Talk about "breaking basic shit".
To continue on that point, I tried to access the app with a screen reader and on Android I just could not.
On Linux I managed to push the "enable accessibility" button, but all the actions in the email app are just "push button clickable" without further description.
They do bring up the on-screen keyboard in the compose window and increasing the magnification in Firefox did work, so there are *some* things they managed not to break.

On my desktop performance seemed good, but on my phone it is super janky. (jank seems to be the favourite word in the Flutter community) Curious to see how performance will hold up with my benchmarks.

#### Schematic

The code for drawing a hundred mosfets was quite okay. I added the `flutter_svg` library, added an a [mosfet](https://en.wikipedia.org/wiki/MOSFET#/media/File:Mosfet_N-Ch_Sedra.svg) to the assets, and generated a hundred SVG widgets inside a `Positioned` inside a `Stack` inside a `InteractiveViewer` for the pan/zoom action.
I don't particularly care about all the details for the moment, I just want to see how easy it is and how well it performs.

Pretty easy, but honestly it doesn't perform that well. In release mode, with a 100 mosfets, it feels like it's dragging along with like 15fps.
In profiling mode it instantly crashes. It actually seems faster in debugging mode though?!
According to the profiler it's running at 30fps when I'm panning around.

![a bunch of mosfets](/images/guibench/mosfets.png)
![flutter perfomance](/images/guibench/flutter_perf.png)

#### Plotting

Of course step one is to locate a library. I checked out several.
The first was very fancy but doesn't seem to offer panning and zooming.
The next gave a compile error because it's not null safe and I'm not a savage.
The next is a commercial package which is a instant no-go for an open source project.
Then I [posted](https://stackoverflow.com/questions/67404580/how-can-i-make-a-line-chart-in-flutter-that-can-be-panned-and-zoomed) on Stack Overflow and moved on.
I also had a look at the Discord server but it seems like the #help channel is just noobs like me asking questions with noone giving answers.

#### Notebook

There are two ways to go about embedding a IPython notebook.
One would be to use a webview, which is ruled out because it only supports iOS and Android.
The other is to implement it from scratch with Websockets, which is what I will do.

I figured I could use the [code_editor](https://pub.dev/packages/code_editor) package, but was once again foiled by null safety.
My understanding is that Flutter 2 does not allow nullable types by default, so packages that have not yet been updated can't be used with it.
I'm sure there are ways to deal with this but for a quick test I just counted my losses and used a plain text field.

The async http and websocket code to connect to the IPython kernel was pleasant, and after a bit of fiddling, I managed to write this simple demo app that once you enter a token and connect, you can execute Python code.

![flutter ipython app](/images/guibench/flutter_kernel.png)

So while I got a good imperssion of async programming, I wasn't able to get syntax highlighting to work, and am worried about how I will handle anything other than plaintext output.
One of the main powers of notebooks is being able to embed figures and even interactive widgets.
Without a Webview to reuse parts of the Jupyter frontend this becomes a difficult proposition.

#### Conclusion

Ease of use is quite okay. Installation was smooth, VS Code integration is amazing, hot code reload is sweet, programming in Dart is more or less fine, and working with async websockets was also fine.

Performance was quite disappointing, with the app unable to keep up the frame rate when panning around with 100 SVGs on the screen.
I was unable to test the performance of the plotting libraries because I was unable to find any that support interactive panning and zooming.

Breaking basic shit: yes. The fact that you can't select text in an email app says it all.
Accessibility is also not great on the web, and in this beta completely absent when building for Linux.

In terms of churn, the ecosystem seems to be in the middle of a big migration to Flutter 2.
Flutter for desktop is also still in beta, so will likely see some breaking changes when it comes to using plugins for file pickers and menu bars.
On the other hand, with a significant community and the weight of Google behind it, Flutter is not likely to disappear anytime soon, and is on the whole a lot less fragmented than the JS ecosystem.