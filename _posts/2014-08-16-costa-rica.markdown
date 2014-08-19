---
layout: post
title: Costa Rica
categories:
- travel
---

### Day 0

My parents dropped me of at Dusseldorf two hours before departure.
I had no checked baggage and checked in online.
Security went really quick, so then I spent two hours waiting at the gate.

When it was my turn to board, they sent me to a different line.
There I was told that my ESTA form was wrong.
I had entered my ID number instead of document number where it asked for my passport number.

I had to fill in a new application, with the gate closing in 10 minutes.
I ran to the nearest internet place, threw more than enough coins in it, and surfed tot he ESTA site.
Everything seemed to take forever and I made a lot of mistakes, but I made it.

The flight was long and boring.
I watched 1.8 movie, listened some music, and talked to my neighbor.
Noah isn't really going to kill his grandchildren, right?!!

Customs, security, more customs, more security. My documents where fine now.
I must say I'm happy Atlanta was not my final destination, not my favourite dialect/culture.

Four hours later I was on my way to San Jose, in a smaller plane with less space and screens.
I did sleep for a tiny bit, which was nice.

I had booked a dorm bed with Alajuela Packpackers for the night.
After some waiting and talking to a guy in a beige shirt, a shuttle from the hotel picked me up.

At the hotel I learned two unfortunate things:

* The large sum of Colon I got from the ATM was worth about $5
* I lost my bag of fluids somewhere. No shampoo and toothpaste for me.

### Day 1

I woke up 4 AM, and did not sleep after.
At 8 AM I got a delicious breakfast on the roof of the hotel.
That cheered me up a lot.

The barista also told me the location of the bus stop and a supermarket.
Getting there was more challenging than expected though.
I'm not used to risking my life to cross a street.
Alajuela is like Manhattan without pedestrian crossings.

The supermarket was another challenge.
I like to eat things like bread with cheese for lunch.
The two things they did not sell where bread and cheese.
I bought what looked most like it and ate it in a park.

I was really scared for the bus to Monteverde.
From what I heard, I was expecting to stand for 5 hours without a toilet break while being robbed of all my stuff.
In reality, the bus was half full of nice people and there was a break in between.

What I did not expect was that most of the way was over unpaved mountain roads.
At one point we had to drive backwards because of an oncoming truck.
It was very scenic. Bumpy roads, small settlements, green valleys, exotic plants.

When I arrived it was already dark.
A taxi picked me up to bring me to the hostel.
After two full days of traveling plus 8 hours time difference, I was at my destination.

### Day 2

I still woke up 4 AM in the morning, but this time I slept a few more hours afterwards.

After eating a bread-free breakfast with my host family, we went to the nearest town for shopping.
It was half an hour on foot. They do not own a car and do everything walking or with a taxi.

There was a farmers market, with fresh bananas and pineapples and vegetables I've never seen before.
After visiting the supermarket (no bread), I started to worry how we would carry all this food home.
I bought a local SIM card and shampoo from the tiny, tiny organic store. No toothpaste there.

We went home with a taxi.

Back at the hostel we ate some fruits, and later lunch with rice, potato, egg and some strange vegetable.
Afterwards I got a pile of their broken devices to fix, because I know things about computers, right?

There was a phone more bricked than a brick, some netbooks with parts missing, burned, or misplaced, and some to old to run a recent Windows.
I'm currently downloading Crunchbang at agonizing speed.

### Day 3

There was a big moth in my room, buzzing against the window.

It's Sunday, so I did not have to work, but I don't know yet what I can do around here, and how to get around.
So I decided to work, and have my free day some other time.

I installed Crunchbang in Spanish onto the working netbook and started looking into unlocking the SIM of a phone.
Every time I had to download something, another 10 minutes passed by.

While downloading OpenOffice and Java, I helped out making a new room upstairs.
The framework was already there, so it was just a matter of sawing planks to the right size and nailing them to the framework.

The house is almost entirely made of wood, and I learned that they built both the hostel and the house all by themselves.
There where some construction workers in the beginning, but due to financial reasons they did the rest by themselves.
I asked if it was hard to get a building permit, but it turns out you don't need one.
That explains the random chaos of building in the towns I guess.

Later, I saw one of the kids with a broken RC tank modeled after the tank in Halo.
I asked what he was doing. Nothing much, it was probably left by people living here before.
(The hostel was their house and the house was rented out before)
I offered to take it apart and try to fix it or use the parts for something else.

Fixing it seemed like the more boring option, and there where no batteries for the transmitter anyway.
So we decided to try making robot using the Raspberry Pi I brought for him.
This is slightly optimistic, as we lack a lot of parts and tools, but we started anyway.

We took it apart completely, yielding a PCB, some LEDs, some motors and a battery box.
We just cut out pieces of plastic from the tank to keep the gearbox around the motors intact.
We then attached cardboard wheels where the broken tracks had been, and glued everything to a piece of wood.

The end result of the day was that we could tape the wires of the motors to some batteries and have it [drive around](https://www.youtube.com/watch?v=IZ2run5p9JU).
If it will ever turn into a real robot depends if we can get tin, wires and transistors to hook it up to the Raspi.

Stores are not so great here when it comes to technology, electronics, organic food, or other things that are not food, clothing and souvenirs.

# Day 4

The kids went to school, so it was a quiet and Spanish day.
I removed the SIM lock from a phone, ate lunch and went to town.

I borrowed a bike because I thought it'd be easier,
but the hills are so steep that I was out of breath after maybe one KM.
The worst thing about being out of breath on the roadside
is that half of the cars are trash with a trail of thick black smoke.

I had a small piece of paper with me with the names of the things I wanted to buy, and how to ask for them.
This worked out okay, and I bought solder tin in Spanish.
I could not find a lot of other things.

After recovering on the couch, the kids came back.
We went to another store to buy a battery to test the transmitter.
It did not transmit.

I studied the receiver for a while, and noticed that every motor was surrounded by a cluster of 6 transistors.
I had a hunch that those transformed two binary signals from the chip to a bidirectional current.
Some googling revealed that they are probably [Tilden H-bridges](http://library.solarbotics.net/circuits/driver_tilden.html).

I located the 2 wires from every motor that led to the chip, and fed them 3V from 2 of the 6 batteries.
This started the motor!
Next we cut the tracks to the chip, and hooked up wires to the Raspi GPIO pins.

This basically turns the receiver into a zombie motor controller for the Raspi.
After some debugging we where able to control the motors using the Raspi!

The only problem is that it's really fast.
We wrote a program that drives 3 seconds, then turns 3 seconds in a loop.
It went completely out of control.

Tomorrow we will look at using PWM to control the speed.
I'll also try to set up the Raspi with a WiFi dongle so we can command it remotely.


### Future

To be continued...
