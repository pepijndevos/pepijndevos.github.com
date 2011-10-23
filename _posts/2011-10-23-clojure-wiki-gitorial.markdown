---
layout: post
title: Clojure Wiki Gitorial
categories:
 - clojure
 - tutorial
---

At the last [Amsterdam Clojurians][1] meetup, I gave a presentation with [Hubert Iwaniuk][2], in which I wrote a wiki in 15 minutes, and Hubert explained what I was doing.

Unfortunately no video is available, but I made a Gitorial out of it to show you what I did. Typos included.

[1]: http://groups.google.com/group/amsterdam-clojurians
[2]: https://twitter.com/neotyk

commit [9d2f91089dadbe795a16da9d84cb0da33cea9a37](https://github.com/pepijndevos/Clojure-Micro-Wiki/commit/9d2f91089dadbe795a16da9d84cb0da33cea9a37)



> Welcome to this gitorial. I just created an empty Leiningen project with
> `lein new wiki` and initiated git with `git init`

commit [e2156be6cbfc421e384ebb016699497501395131](https://github.com/pepijndevos/Clojure-Micro-Wiki/commit/e2156be6cbfc421e384ebb016699497501395131)



> I'm using Ring for serving my application, and Moustache for routing.

commit [1898d45e54d1ae6045742f8e5db3b8fbc1fb7141](https://github.com/pepijndevos/Clojure-Micro-Wiki/commit/1898d45e54d1ae6045742f8e5db3b8fbc1fb7141)



> I defined a router that will at this point match an empty address and
> return "hello world". The -main function can be run with `lein run -m
> wiki.core`.
> 
> Browsing to <http://localhost:8080/> now should display "hello world".

commit [5e68a78f30586ad5313072201df25e5b1eea76ac](https://github.com/pepijndevos/Clojure-Micro-Wiki/commit/5e68a78f30586ad5313072201df25e5b1eea76ac)



> A lot is going on here, form top to bottom:
> 
> I imported some Ring middleware. These modify the request and response on the fly.
> Note that wrap-reload and wrap-stacktrace are for debuging only.
> 
> I added the middleware to the Moustache app.
> 
> I added routes. The first route matches and WikiLink and binds it to
> title. The second one redirects all other links to the MainPage.
> 
> The #' syntax is for getting the var instead of the value, to make
> reloading work.
> 
> At this point, visiting the same url should redirect to /MainPage and
> display "hello tester"
> 
> Changing this text does not require a server restart, so we can keep it
> running from now on.

commit [ae78c9d640420d0154b22c66df581fa684b8aa48](https://github.com/pepijndevos/Clojure-Micro-Wiki/commit/ae78c9d640420d0154b22c66df581fa684b8aa48)



> I lied, to add a new dependency, you need to restart the server.
> 
> Hiccup is a DSL for generating HTML.
> 
> I defined a HTML template and a function for showing it that takes a
> request and a title.
> 
> Note that I used the underscore to denote we're not using the request.
> 
> Delegate is a HOF that returns a function with the title alreadu
> supplied. Moustache supplies the request.
> 
> Try visiting /FooBar now.

commit [cd24f552f7eefb470ac7b4357d01486e0fc9c888](https://github.com/pepijndevos/Clojure-Micro-Wiki/commit/cd24f552f7eefb470ac7b4357d01486e0fc9c888)



> I added Clutch as a dependency. Clutch is a library for CouchDB.
> 
> Install CouchDB and use Futon to create a wiki database and insert a
> "MainPage" document with a "content" key.
> 
> I defined my own Ring middleware that takes a handler and returns
> another function that calls the old handler in the context of our
> database.
> 
> The show function now gets a document from the database and passes its
> content to page.

commit [f0c831708f397137b9aadf397376de2202f5cd60](https://github.com/pepijndevos/Clojure-Micro-Wiki/commit/f0c831708f397137b9aadf397376de2202f5cd60)



> You can now edit and create pages.
> 
> Page now takes a revision, which is used in the web form to update.
> 
> The update function does use the request object, and uses destructuring
> to extracts form data as parsed by wrap-params.
> 
> Depending if a revision was supplied, a new document is created or an
> existing one updated. Then, the page is shown.
> 
> Note how Moustache now delegates POST requests to update and GET
> requests to show.

commit [11588b8f1e591ea6415515ffa21e70ed630eef91](https://github.com/pepijndevos/Clojure-Micro-Wiki/commit/11588b8f1e591ea6415515ffa21e70ed630eef91)



> I added a Java library for Markdown parsing.
> 
> The markup function also replaces WikiLinks with an HTML link.

commit [235c551d57786f79c1867452de37b079a59606bd](https://github.com/pepijndevos/Clojure-Micro-Wiki/commit/235c551d57786f79c1867452de37b079a59606bd)



> Getting ready for deployment.
> 
> I removed the debugging wrappers and the var syntax(#'wiki).
> 
> Jetty now gets the port number from the environment/Foreman. This means
> it now runs at port 5000.
> 
> Install Foreman with `gem install foreman`
> 
> I added a Procfile according to
> <http://devcenter.heroku.com/articles/clojure>
