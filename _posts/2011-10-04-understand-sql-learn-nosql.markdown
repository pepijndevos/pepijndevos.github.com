---
layout: post
title: Understand SQL, learn NoSQL
categories:
 - sql
 - nosql
 - couchdb
---

I learned SQL when I started PHP. I found a website named [Tizag][1], where they had SQL tutorials. I installed PhpMyAdmin, created tables, ran queries like `SELECT * FROM pages WHERE foo IS bar LEFT JOIN ON comments` or whatever. It was magic.

No one ever explained to me how it stored the information, or how it was so fast (or slow). They did say that indexes made stuff faster, sometimes.

On the other hand, when you read the CouchDB guide, they do not primarily teach you their query language, but also a lot about how stuff works. A lot of this also applies to SQL databases.

### Storage

CouchDB uses a [B-tree][2] to store documents. This provides O(log n) lookup, update, etc. rather than O(n) scanning of all documents. It seems most SQL databases use a B-tree as well, but not always.

### Indexes

When you add a `WHERE` clause to your query, the database has to look at all documents for a match.

If you add an index to the field, you get a sorted representation of that field. This way you can get single items or ranges(`time > 123456`) in logarithmic time, using binary search.

CouchDB gives you a 'view' of the `_id` of a document, but other views will have to be created to create the equivalent of a `WHERE` clause. (What `_id` is in CouchDB, is your primary key in SQL)

### Locking & Transactions

NoSQL databases are infamous for their lack of locking and transactions. Why? For the sake of scalability.

Let's ignore for a moment that 90% of all apps can run on a single server. The idea is that creating a transaction synchronously on a whole cluster is nearly impossible, let alone fast. So you you just don't to it at all, in NoSQL land.

The flip side is that not locking at all allows reads to be faster. More on that in the next section.

Interesting to note is that both CouchDB and PostgreSQL use [MVCC][3], allowing for reads without locking. So this is not unique to NoSQL databases.

### History & Recovery

CouchDB stores its data in [append-only B-trees][4], meaning that data is never changed.

Because old data is still there and immutable, readers can access it without waiting for a write to complete. It is even possible to read old revisions of the data.

What is maybe even more interesting is that, if the server crashes in the middle of an update, the old data is still there.

InnoDB also applies a similar technique, unlike Mysam, which needs to scan and repair the whole database.

### Joins

Basically the same thing as transactions, you don't want to scavenge your whole cluster looking for all comments referencing a blogpost.

The high-performance way to do joins is to not do joins, instead unlearn everything your learned about normalization, and [denormalize][5].

The other way to do it teaches us more about SQL joins though. Basically you create an index or view on the 'foreign key', and run a separate query to get the correct documents. Here is an [elaborate example][6].

### Conlusion

Neither SQL or NoSQL databases are magic, and they are even pretty similar in most ways. Don't follow the hype, choose wisely.

[1]: http://www.tizag.com/mysqlTutorial/
[2]: http://en.wikipedia.org/wiki/B-tree
[3]: http://en.wikipedia.org/wiki/Multiversion_concurrency_control
[4]: http://www.bzero.se/ldapd/btree.html
[5]: http://en.wikipedia.org/wiki/Denormalization
[6]: http://www.cmlenz.net/archives/2007/10/couchdb-joins