---
layout: post
title: My girlfriend in Rust
categories:
- rust
---

Some people don't like to say "my girlfriend".
They think it implies ownership over the person.
Lets explore that using Rust.
Swap genders at will.

{% highlight rust %}
struct Human {
    name: str
    // ???
}
{% endhighlight %}

Even though Rust is not object oriented, I'm sorry for objectifying Alice here.

{% highlight rust %}
let bobs_girlfriend = Human { name: "Alice", /* ??? */ };
{% endhighlight %}

This is what some people think when they hear "my girlfriend”, Bob owns Alice in this cenario.

{% highlight rust %}
let charlies_friend = &bobs_girlfriend;
{% endhighlight %}

Charlie can only borrow Alice, Bob maintains ownership of Alice, the compiler enforces monogamy.

That is not how it works, lets try again.

{% highlight rust %}
use std::rc::Rc;
use std::option;

let alice = Rc::new(Human { name: "Alice", /* ??? */ });
{% endhighlight %}

The name "Alice" refers to Alice, who is now owned by the reference counter.
In this digital world it's like a god, it decides who lives and dies.

{% highlight rust %}
let mut bobs_girlfriend = Some(alice.clone());
{% endhighlight %}

"Bobs girlfriend" is a reference to Alice, no more or less than the name "Alice".
Bob owns the reference, but not Alice.

{% highlight rust %}
assert!(*alice == *(bobs_girlfriend.unwrap()));
{% endhighlight %}
"Alice" and "Bobs girlfriend" are the same thing, though the latter is mutable and optional.

{% highlight rust %}
assert!(*(bobs_girlfriend.unwrap()).beautiful == true);
{% endhighlight %}
This throws a compiler error; Beauty is in the eye of the beholder.

{% highlight rust %}
let mut charlies_girlfriend = Some(alice.clone()); // Polygamy
{% endhighlight %}
Charlie does not own Alice either.

{% highlight rust %}
charlies_girlfriend = None
bobs_girlfriend = None
{% endhighlight %}
If you are forgotten, do you cease to exist?
Better not find out.
Love the ones dear to you.
You'll never know when they will be garbage collected.

