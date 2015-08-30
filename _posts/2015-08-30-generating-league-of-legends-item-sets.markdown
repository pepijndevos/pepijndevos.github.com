---
layout: post
title: Generating League of Legends item sets
categories:
- gaming
- clojure
- leagueoflegends
---

I'm not a very dedicated League of Legends player, and after several months, I'm still unranked and basically build whatever is recommended on a champion.

When I saw the [Riot API challenge](https://developer.riotgames.com/discussion/announcements/show/2lxEyIcE), it seemed like a perfect opportunity to learn more about items and share this knowledge with other players.
So I set out to [make a tool that analizes item stats and generates optimal builds](http://pepijndevos.nl/LolItemSets/).

Easy enough right? Well, not really. Doing an exhaustive search on all item combinations is prohibitively expensive, and it's not always clear what is "optimal" if you want both attack damage and armor.

The collection of problems where you try to find an optimal combination of something are called "optimization problems", and when you have more than one thing you want to optimize, you have a "multi-objective optimization problem".

I'm not exactly sure what this means, but insert some text about polinominal time and P=NP here. Basically, we don't have any good solutions for problems like these. There are some heuristics though.

One of those heuristics is simulated annealing. This is something borrowed from another dicipline, so they use terms like "temperature" and "energy". But really you have a function that randomly permutes a state, and a function that computes how "good" that state is. If the new state is better, continue. If the new state is worse, maybe continue. There is a decreasing variable which determines how likely maybe is. The idea is that you start bouncing around, slowly becomming more conservative, converging on an optimal solution.

It is worth noting that when you have two conflicting objectives, "better" becomes hard to define. If you sum their measures, you tend to find an extreme that only satisfies one of the two. Instead, what I do is that I only consider a set better if it's better on all objectives. If it's worse for one objective, it's a definite maybe. This tends to generate fairly balanced results.

You might wonder why not simply pick the "best" items and add them together. This would surely be faster, right? Yes. This is called hill-climbing. The problem with it is that you might end up in a local maxima. A set of items that gets worse if you change one item, but that's not the absolute best item.

Consider you have a sword with a ton of damage, an armor with tons of defense, and an item with a bit of both. The hill climbing is likely to continue picking the mixed item, while a build that consists of a mix of the first two items might be beter overall.

So after figuring this out, I downloaded the champion and item data from Riot and started writing objectives to optimise for. I started with damage per second and effective health. Damage per second is the product of your attack damage, attack speed and crit chance. Effective halth is the damage it would take to kill you give your healt and armor.

The first DPS build included two attack speed items like Phantom Dancer and Zephyr, with the rest filled with Infinity Edges. This does about 3K damage per second, but does not include any defense or life-steal. It also completely ignores the item abilities.

This lead me to modify the code to include each item only once. But it remains the case that no matter what, attack speed(2.5/s) and crit chance(100%) are nearly maxed out in all builds.

Effective health had a similar issue with Randuin's Omen and Banshee's Veil, in addition to Warmog's Armor. Builds would include one or two Warmog's Armor with the rest filled in with the other two, depening if you chose magic resist or armor.

Even after restricting to one item of each, builds include a lot of health in addition to armor/magic resist. One point of armor gives you 1% extra of effective health. So 100 armor doubles your health, but if you have 100 armor, Warmog's suddenly give 1600 effective health!

But things start to get really interesting if you start to combine objectives. I learned about a lot of new items this way. There are quite some underused items that provide a combination of things. Itmes like Abyssal Scepter and Twin Shadows. Not even my brother (Platinum V) knew about Twin Shadows.

On the other hand, there where also instances where I showed my brother a set, and he knew a better item, or told me that item was not available on Summoners Rift. There where occasions when items like Lord Van Damm's Pillager would show up, or Bloodthirster would be missing because its life-steal was not in the data.

To solve this problem, I created an "overlay" to which I could add data about an item. This works great, but there are still gaps in the data. Another issue is that some item abilities are hard to put a value on.

I also made some key functions multimethods that dispatch on various things. This allows me to encode things like Yasuo's double crit change and Singed's health from mana. But this is currently implemented for very few champions.

In its current state, it works fairly well for tanks and champions seeking raw damage output. It does not work so well for utility supports.

I don't care a lot about the prizes, because I have an [awesome keyboard](http://atreus.technomancy.us) already, but it would be fun to win, and I hope some people find it useful.
