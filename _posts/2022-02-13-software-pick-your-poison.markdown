---
layout: post
title: "Software: Pick your Poison"
categories:
- software
---

I'm a jack of many trades master of some, who has worked his way down the stack from frontend to chip design. I'm in my early thirties, so officially old in terms of software (jk but startup culture can be kinda ageist I've heard), so I feel somewhat qualified to give a "30000 foot view" of the different disciplines in software development.

I'll briefly describe my (in some cases limited) experience with each, and then break them down in the good, the bad, and the ugly. The exciting stuff, the frustrating stuff, and the bread-and-butter seen-one-seen-all stuff.

This post was inspired by someone on Twitter getting into "software", meaning frontend JS, which made me feel like I maybe have some useful perspective to share on why you might want to do frontend, or what else you could be doing.

### Frontend

I started my career building Wordpress websites, back when jQuery was cool. Later I had a gig working in Angular.js and did some React hobby stuff. I'm currently building [Mosaic](https://nyancad.github.io/Mosaic/) in Reagent (a ClojureScript React wrapper).

Frontend problems are all about design and state. You make a user interface and then hook up the buttons and fields to state, and communicate that state to the backend.

The good part in frontend is in my opinion that you can very quickly get a tangible result. If you think software is changing pixels on the screen, this is close to that ideal. It's also a part of software very close to the user, and it obviously has a big design aspect. It's very easy to get started with, there are lots of resources, and it's easy to land a job.

The bad parts of frontend basically all stem from the fact that everything has to run in a browser. It's maybe not as bad as in the IE6 days, but you still have to fight browser compatibility bugs and limit yourself to the lowest common denominator in features. It also means JavaScript, the language that was famously invented in 10 days, and will plague us for the rest of eternity.

People have been building things that improve on JS and CSS (SCSS, Sass, TypeScript, CoffeeScript), and then use transpilers and bundlers to convert this into the lowest common denominator stuff suitable for browsers. This, combined with its immense ubiquity, has lead to a sprawling ecosystem of boilerplate spewing tools that changes continuously. The tool I used for the Angular gig doesn't even exist anymore. To me the churn and boilerplate is what I despise most about the frontend ecosystem.

But the ugly part is that most of the work is pretty similar from the tech side. Every Wordpress theme is technically almost identical. These days JS apps are a bit more involved, but still, one CRM dashboard isn't going to be that different from the next CRM dashboard. The appeal is probably more in the design aspect, working with customers.

### Backend

As said, I started doing Wordpress. After that I worked at a company on modernizing their Perl backend, writing Python APIs and workers. I also did some hobby backend stuff in Clojure.

"Backend" is very broadly everything web related that's on the server. I'd say the main thing is implementing HTTP endpoints that implement business logic and communicated with a database. It doesn't have the visual aspect, but not being constrained to the browser grants a lot of freedom. You can use literally any programming language out there, and most of them have good web frameworks.

So sorta the good thing is that there are not a lot of bad things. There are bad parts but you can mostly avoid them.

If I had to pick a bad thing, I'd say deployment. Back in the day you'd just throw some PHP in your shared host LAMP stack and that's it. Now everything has to be web scale and cloud native and what not. That brings with it headaches like eventually consistent databases that throw away your data, and apparently writing lots and lots of YAML to configure your fleet of containers, and then getting a &euro;50k bill because you clicked the wrong button on AWS.

The ugly part is just never ending CRUD (create, read, update, delete) apps. Sure, there are interesting problems in backend but the bread and butter stuff is HTTP endpoints that create, read, update, and delete data from a database. Better get cozy with SQL. (or an ORM if you want to invite the scorn of the SQL experts)

### Systems programming

At my current job I'm working on a SPICE simulator and a C++ RPC thing. I've also written some toy compilers and databases and stuff.

Ok this is an extremely nebulous definition, but like... everything that runs on a normal operating system and is not web related. It's all the tools and services used by the backend people and more. Roughly speaking, if its leading characteristic is performance, it's probably systems stuff.

The good is that this domain has lots of hard and interesting problems, with lots of room for implementing clever algorithms and data structures, and optimizing stuff for maximum performance. Like backend, you have a choice in what tools to use, but there is a strong bias towards the more low-level and high-performance ones. Expect lots of C/C++. And Rust if you hang out with the cool kids.

The bad is no doubt build systems. Being more low level exposes you more to operating system differences, so in a way it's a bit similar to the frontend browser compatibility stuff, but much more unchanging. The build systems you'll run into are pretty much Make, autoconf, and CMake. Always have been, always wil be, sorta. You'll spend endless time making your software build on Linux, OSX, and in particular Windows, and then compiling all your dependencies on all these platforms.

The ugly is that there is never as much of the hard and interesting problem as you hoped, and lots of stuff around it. A database has a high performance storage part, but also a network API and config files. A compiler has interesting compiler bits, but also needs a library of bog standard functions. A simulator has an interesting mathematics bit, but also needs a library of models to work with. It's no surprise that hobbyists tend to build game engines and then never build the game.

### Desktop software

Had a job building a teleoperation platform in Qt. Also some hobby apps.

Briefly put, desktop software is like a mishmash of frontend and systems programming. The objectives are visual like frontend, but the environment is the OS, like systems programming. You can spend hours optimizing pixels, and [days getting dependencies to build](/2018/10/02/qtgstreamerddsandroid.html). Most UI toolkits are written in C/C++, with wrappers available for other languages. You can write GUI apps in Python for example, but then you have to ship Python to your users, which is a PITA. Also, the desktop is kinda getting taken over by the web. Lots of serious tasks can these days be done in web apps or Electron apps, and React-style development is usually more pleasant than imperative UI toolkits.

I guess game development also fits in here, which I don't really have experience with. It's also graphics + performance + low level, but has quite a different dynamic to it from what I've heard. It looks glamorous from the outside, but I would recommend to read some first hand accounts from game devs, because it can be gruesome and toxic.

### Scientific computing

I have a degree in electrical engineering and IC design. Lots and lots of small experiments.

This one sits more between backend and systems programming, in that there are a diverse set of hard problems to solve, but you tend to do them in more high-level languages like Python, Julia, Matlab, or R. It also has a big data visualization aspect, but there isn't as much design involved because it's mostly tucked away in neat libraries. Usually it's not so much a software job, but a science job that requires software as a means to an ends. Lots of this stuff is written by physicists who don't necessarily have a software background.

I guess machine learning is also scientific computing? I know literally nothing about it, but people seem excited about it. Here too it's probably the case that there is a small core of really interesting science, but also a lot of feeding data to an API (PyTorch etc.) and visualizing the results.

So the good is that you're probably doing science and getting cool results. The bad is that you're probably dealing with code written by scientists. The ugly is that it's a lot of just cleaning up data and plotting it.

### Embedded

Did some gigs involving microcontrollers, an internship in FPGA tools, and a metric ton of hobby projects involving Arduino's and FPGAs.

I'd define embedded as software that runs without an operating system. I'll also throw PLCs (industry automation) in this category, because they seem to kinda have the same properties and I don't know a huge deal about them. The main objectives in embedded are resource usage and latency. (if you would not have these requirements, you could just run Linux and do whatever)

Certainly at the hobby level, embedded has the same immediate tangible results as frontend. You write code and the LED blinks or the robot moves, very rewarding. You're also in complete control over your environment, you can build your own castle from scratch by poking registers, and justify the ugliest handwritten assembly because of latency and low power or whatever.

The bad is the tools. Outside of the happy lalaland of Arduino and embedded Rust, there are cases where you get some GCC fork in a hacked up Eclipse IDE and good luck. In FPGAs open source tools are even less common. For hobby stuff you can certainly get an ECP5 and use Yosys and Nextpnr or whatever, but chances are if you get a job as an FPGA developer you'll be using proprietary tools by the FPGA vendor.

The set of languages available for embedded stuff are also pretty much C and more C for microcontrollers and Verilog or VHDL for FPGAs. There are of course cool languages like Rust and Amaranth HDL, but the industry hasn't really picked up on that yet. No experience with PLCs but from what I've heard they also have a pretty limited set of languages and vendor tools.

The ugly is probably that in an industrial setting, you're probably not building glamorous robots but maybe just some glue logic that reads out a sensor over SPI and if it goes above a certain value sends a message on the CAN bus.

### Conclusion

Every branch of software development has rewarding parts, frustrating parts, and some amount of mundane drudgery. Some may suit you better than others. If you find a thing with only good parts, do share ;)

One thing that stands out that if you do it as a hobby you can avoid some of the bad and ugly parts. You can just pick up the hot new tools and do only the interesting bits. If you're doing it purely as a hobby it's also worth mentioning retrocomputing and programming as art. You can certainly have a ton of fun [hacking Game Boy's](/2017/08/06/introduction-to-game-boy-hacking-at-sha2017.html) or marking art with code, but these are not typical career paths.