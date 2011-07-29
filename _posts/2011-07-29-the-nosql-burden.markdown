---
layout: post
title: The NoSQL Burden
categories:
- nosql
- clojure
- couchdb
---

Some people claim that [NoSQL is  premature optimization][1] and places an extra burden on the developer. The main points seem to be that NoSQL has no schemas and drops consistency in favour of the other parts of [CAP][2](availability, partitioning).

I largely agree, however, I think we should differentiate between two kinds of NoSQL databases.

1. ScaleDB: Hard-core MapReduce, thousands of nodes, sacrifices everything for speed and scalability. If you use this below Google-size, you have [no idea what you're doing][3].

2. EasyDB: Relax, life is to short to update your schema and master SQL, give me an easy API to persist and query my data and I'll be happy. I really think some NoSQL databases should realize this and drop scalability as a prepackaged buzzword feature and focus on the real needs of the majority of their user base.

Relax, does that word ring a bell? It's the tag line of CouchDB. With its  nice REST API and with replication as the only way to scale horizontally, I think CouchDB classifies as an EasyDB.

However, CouchDB uses [MVCC][4], this avoids locking and provides a form of consistency, but it does place the burden of handling update conflicts on the client. Or... does it?

![CouchDB concurrency](/images/couch-concur.png)

I would like to draw a parallel with how Clojure handles controlled shared mutable state. The simplest form present in Clojure is the [Atom][5].

An atom is a MVCC construct that provides a low-level `compare-and-set!` function that executes an atomic update if the expected old value matches the actual value. Much like CouchDB compares the `_rev` of a document before updating.

Interestingly, though, atoms also provide the very convenient `swap!`, which takes a pure function that takes the old value and returns the new one. `swap!` calls `compare-and-set!` in a loop, recomputing the new value on every iteration until the update succeeds.

So what about CouchDB? Can we have easy fire-and-forget updates there as well? Yes we can! I previously hacked together an [atom implementation on top of CouchDB][7], but it turns out CouchDB already offers a little known feature called [Document Update Handlers][6], which does exactly this.

Sadly, the Clojure view server included with [Clutch][8] does not yet include support for document update handlers, but this can be easily remedied!

[1]: http://smoothspan.wordpress.com/2011/07/22/nosql-is-a-premature-optimization/
[2]: cap
[3]: web scale
[4]: http://en.wikipedia.org/wiki/Multiversion_concurrency_control
[5]: http://clojure.org/atoms
[6]: http://wiki.apache.org/couchdb/Document_Update_Handlers
[7]: https://github.com/pepijndevos/couch-atom
[8]: https://github.com/ashafa/clutch
