---
layout: post
title: C persistent hash map with Python bindings
categories:
 - c
 - python
 - cython
 - clojure
---

After using Redis for a bit, and wanting to learn C, I set out to write a database that uses persistent collections and allows arbitrary keys.

It turns out you need to reinvent a lot of wheels to write a big application in C. By the time I realized that, I had reinvented object orientated programming, reference counting and a peristent hash map.

I'm not sure the database will ever see the light of day, but my persistent hash map works well enough that I made it into a C library with a Python extension.

The hash map uses the same algorithm as Clojure. A good explaination of how it works can be found [here][1].

I implement the different node types as what are best described as polymorphic structs. Structs that share the same header as their superclass, so they can be casted back and forth. Example:

    struct foo {
    	int head;
    };

    struct bar {
    	int head;
    	int tail;
    };

    struct bar *test = malloc(sizeof(struct bar));
    test->head = 3;
    test->tail = 6;
    ((foo*)test)->head; // 3

Now I define different node types, that share a header of function pointers to insert, delete and find functions.

Currently, putting a node in a node as a key or value would segfault, but this is only because the function pointers for equality and hashing point into empty space(or kernel space even).

After I had the C code working, and played around with it for a bit, I realized I needed an intelligent way to free memory, so refcounting was reinvented.

Basically every polymorphic struct has a refcount and a free function pointer. I simply define 2 functions to increment and decrement it, the latter calling obj->free if it becomes zero.

The next important discoveries where that C is verbose and Python does not have persistent hash maps. This led me to develop a Python extenson to cure both problems.

First I looked at ctypes, which would be the easiest and most portable. Unfortunately, my datastructure needs to store Python objects, which can't be done in ctypes.

Next up was a real C extension. This is not too hard for functions, but defining a class is verbose and hard. That is when the nice folks in #python suggested Cython.

I ended up writing an PyObject wrapper in C, to allow Python objects to be nodes in the trie, but wrote the Python interface in Cython. Except for some casting issues, it's pretty sweet.

I encourage you to check out the [Github page][2], it has silly benchmarks. Clojure is still faster than my C code, but my C code is a lot faster than other Python code.

[1]: http://blog.higher-order.net/2009/09/08/understanding-clojures-persistenthashmap-deftwice/
[2]: https://github.com/pepijndevos/perper
