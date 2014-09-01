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

Later, I saw Gudari with a broken RC tank modeled after the tank in Halo.
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

### Day 5

I tried really hard not to finish the robot before Gudari came back from school,
but I did repair the wheel and set up WiFi on the Raspi.

I spent the rest of the morning not doing ver much,
helping with the new room upstairs,
and checking out the suroundings.

When Gudari came back, we tried the PWM and creted some programs.
Then we decided to make a bumper out of some paper and tinfoil.
So now the robot can [drive around](https://www.youtube.com/watch?v=pERpIgb1OWc) and back away from obstacles.

Finally, I hacked together a Flask app to act as a remote control using a smartphone.

In the evening I practiced some Spanish.

### Day 6

This morning Tarsicio told me I should take some more free time, I'm working too much.
Problem is that I havn't really figured out what to do with my time.
I also like working here.

After brekfast I went to the nature reserve close by.
It had really big trees, birds and butterflies.
I also saw a squirl and some dog-sized animals that ran away before I could really see them.

After lunch I went to a waterfall and sat on a rock thinking and looking around.
I returned home when it started to rain.

In the evening I played a game with Julia.

### Day 7

In the morning I fixed the piano.
The pedals where not working correctly.
I printed Gymnopedie 1, which I practiced for a bit.

Afterwards worked on the new room until I started to lose focus and make mistakes.
I'm really perfectionist, but the wood is not first class, neither are the tools.
So it's really hard to make everything fit exactly.

After lunch I made another trip to the village.
I'm getting better at cycling in the mountains,
but I was still exhausted when I got back.
Without toothpaste.

Tarsicio previously told me he had an old Mac he wanted to fix.
It's an iMac G3, to which he forgot the password.
I booted it in single user mode and added a new user.
Then I looked for the pictures he said where on it, but did not find anything.

When I clicked "About this Mac", I realized that this machine is slower than a Raspberry Pi.
A 600Mhz G3 processor and 320MB RAM.

The internet does not work. Neither wired or wireless.
I'm not sure if it's a hardware or software problem.
I tried installing various Linux distributions in various ways,
but the G3 is so old and PPC so little used that it's hard to find anything.

It was already dark, but not dinnertime yet.
I played some more piano and did a moon mission in KSP.
For dinner we had soup with popcorn.

### Day 8

Today was the birthday of Gudari, so I decided to make a cake.
I cycled to the supermarket to buy stuff.

The cream didn't really work because it wasn't cold.
So I spent a bunch of time playing piano and KSP while I put the cream in the refridgerator.

After the cake was done I went to the Bat Jungle, run by a Flemish guy.
He spent a bunch of time busting myths about bats, and raising awareness about their importance to the ecosystem.

Basically they are closer related to primates than birds or mice. They are intelligent and social.
Half of them eat insects, the other half eat fruit.
A few kinds live from blood, but... not from humans and they don't carry diseases.

In Costa Rica there are more bats than birds, but you don't see them during daytime.
They are super important for polination of many tropical fruits.
But they are endangered by deforestation and green energy(!).

I was surprised to learn that if you disturb a bat during daytime or winter(hibernation), it basically dies.
As soon as it starts to fly it uses so much energy it needs to eat insects constantly.
But without its usual prey, it just starves.

Then he fed them, and we could see them fly around and eat fruit.
He showed the different kinds.
There where hummingbats that could hover like a hummingbird.

Afterwards I cycled to town to buy a chicken for dinner.
Some of Gudari's friends came over, we ate cake and played Minecraft.

### Day 9

We went to the farmers market, and I bought ingerdients for some Dutch food.
Then we went walking to another place to get wholegrain flower.
They where friends of Tarsicio, so he talked for a long time.
I picked up the 25Kg bag and walked home.

There I played some Minecraft with Hudari and one of his friends.

I had heard from an American family that they had seen monkeys at the Cloudforest Reserve.
I decided to go there by bike, but it was 3Km uphill.

By the time I got there I was so exhausted I didn't feel like paying $20 to walk 2 hours.
It also looked like a storm was comming.
So I visited the hummingbird garden and returned home.

Hummingbirds are pretty cool.

### Day 10

Today breakfast cosisted of a cross between chessnut an pumpkin.
I tried a few, and then baked an egg.

What I wanted to do today: Walk in the nature for hours.
What I did: Nothing much, my feet hurt.

I wrote an ad for selling the house.
They plan to live in the first floor of the hostel,
and the build a new sandbag house, as I understand it.

Some friends of Tarsicio came over to watch The Mill and the Cross.
A movie about Pieter Bruegel while he observes daily life and describes and paints it.

I messed with the Wii to make it play a copied DVD,
it did not work, so we watched on a laptop.

For diner, I baked pancakes with cheese.
How weird, pancakes for dinner, and with cheese?
For the first time in history I baked more than needed. 

### Day 11

Bread!!!
Or at least wheat-flower-based things.

I gave my feet another day to recover, so not much action.

We put the [ad for the house](http://www.encuentra24.com/costa-rica-en/real-estate-for-sale-houses-homes/casa-alquimia/4003692) online.
There where some new guests in the hostel from Germany. I talked with them a bit.
Then I distracted the kids from making homework for a while.

In the evening the guests talked Spanish with Tarsicio for a long time.
I barely understood a quarter of it, but still learned some things about this place Tarsicio never bothered telling in English.
This really shows the power of speaking someones native language.
I should really learn more Spanish.

### Day 12

I did not see my laptop for a few days, as you'll see. So some details are mising from my memory.

I decided to visit the Pacific Ocean for a few days, so I spent most of the day booking a hostel, planning, and packing stuff.

All the things on AirBnB where kind of expensive, but I decided that for 2 days I shouldn't worry too much.
So I ended up booking a room in this Jungle Villa which is exactly what the name implies.

I borrowed a backpack from Tarsicio so I could leave most of my stuff here.
I only took some clothes, my phone, and my wallet. No laptop.

### Day 13

This was a scary day and I did not enjoy it at all.

The bus left 6 in the morning, so I got up at 4.
The hostel guests where also going in the same direction, so we traveled together for a while.

They don't exactly publish bus schedules here,
but I was told there would probably be a bus in Puntarenas to Manuel Antonio.
I was so nervous I had to pee all the time.

In the bus I met another couple traveling to Manuel Antonio.
We chilled for a bit at the beach in Puntarenas, and then they where getting some lunch.
I said "I'm staying here, I hope I don't miss the bus".

When it was time to go, I saw the German couple, but not the Americans.
I went looking for them, but did not see them.
I hardly made it in time myself, the bus was already in motion when I jumped on.
I'm glad they drive with the doors wide open over here.

The bus stoped in Quepos, where I took a taxi to the hostel.
The location of the hostel is best decribed by its GPS coordinates,
so the driver didn't have an easy job finding it.

When I got there I found no one speaks English there, so asking for directions was tough.
Aparrently the place also doesn't have a phone, other than that of the Dutch owner.
I was the only guest, and stayed in this huge room, next to a huge swiming pool.
I used neither very much,

I decided to go to the beach in the afternoon.
I asked for directioins, and they pointed me to the bus.
I had no idea where to get off or how to find my way back to the hostel.

I spent a bunch of time messing around in the ocean and sitting on the beach.
It was clouded, but still 30 degrees.
The water was really warm and there where really strong waves.

When I started to look for an ATM and the bus stop back to the hostel,
I met 2 German girls, they gave me some directions, we talked for a bit,
and then we took the bus together.

When my stop came, they asked if we could meet tomorrow, while the bus driver was yelling at me in Spanish.
This was too much for my poor nerves, so I said something useless and got off the bus.

### Day 14

Having figured out the hostel, bus, food, and money, I was much more relaxed.

In the morning I went to the nature reserve.
The most animals I saw where in fact of the same species as myself,
usually trailing a guide with a telescope on his/her shoulder.

When I got off the main road it became more quiet.
I saw a Sloth, a deer, lizards, two kinds of monkeys, butterflies, crabs, and more.

I walked all the way to the farest beach in the park, which was practiclly empty.
It was in a small bay, so the waves where much lower.
I put on some sunscreen for the first time in years and spent a few hours there.

Afterwards I drank from a fresh coconut and did nothing for a bit.
I'm spending so much money here, so I had to go back to Quepos to get some cash.
I also bought some lunch there.

Back at the beach I rented a surfing board.
First I learned how to avoid drowning and getting hurt.
Then I learned how to catch a wave and fall.
Next I learned how to stand up and fall.

Two hours later, cut, bruised, exhausted, I managed to actually get up and surf for a few seconds!

At the end of the day, dark clouds started to appear.
When I got back to the hotel, it started pouring.
For dinner I dashed to the nearest place, and ate a pizza.

### Day 15

I got up early for the bus back to Puntarenas.
I arrived at the bus station in time for a non-existant 8:30 bus,
so I had to wait a long time for the 9:30 bus.

I was hoping to take an earlier bus to feel more relaxed about the connection in Puntarenas.
There is only one bus to Monteverde per day, or so I was told.

That was one boring and uneventful day.
After hours and hours of sitting in a bus,
I arrived, played Monopoly, ate diner, watched a movie, slept.

### Day 16

I feel like I'm missing a couple of days. I should have used dates instead of sequential numbers.

While I was away, the internet had stoped working all the time.
There is a modem in the hostel, then a wifi router, and then a repeater in the house.
All the steps from the ISP to the repeater fail all the time.

I did a bunch of research, and found that WPA2-AES is known to work.
So I set up DD-WRT as a repeater bridge using that.
It still failed all the time and DHCP did not work.

After a long time I found out my host had loaded a buggy version of DD-WRT on the router,
which was known to randomly disconnect and break DHCP forwarding.
After flashing a newer firmware everything started working.

### Day 17

Gudari asked me if we could attach the Raspi to the TV.
I told him we need a RCA cable, which we could not find.
We did find some other useless converter cables which we soldered together.

Then we messed around getting WiFi to work, connecting a DVD player, playing Python games.

We found that not all of them worked correctly, or the way we liked them, so we started hacking.
While he doesn't really understand the flow and structure of the games yet,
we where able to chnge the size, speed and some other parameters.
We made snake starts slow and then get gradually faster.

When I got tired of looking at this flickering low resolution CRT screen, I continued building the room upstairs.

In the evening I was showing the wooden mouse I made to Tarsicio,
and he said it would e fairly easy to make one out of one piece of solid wood.
So if we we can find enough people that would want to buy one, we will be making a few.

### Future

To be continued...
