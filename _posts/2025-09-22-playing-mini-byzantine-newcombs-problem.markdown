---
layout: post
title: Playing Mini Byzantine Newcombs Problem
categories:
- rationalism
---

It seems that people on my part of Twitter, in particular the rationalists, are very into Newcomb's problem, probably because their guru wrote about it, so they all believe you should pick one box.

So I thought it would be a fun idea to take two boxes and some money to Treeweek 2 and try to actually play Newcombs Problem.

### Rules of Byzantine Mini Newcomb’s problem

This is a thought experiment made real, as a thought experiment about what it means to really play this game. What’s up with the name? Mini: I am not made of money so we are playing at reduced stakes and on a donation basis. Byzantine: How do you tell Omega from a charlatan? How reliable are my predictions really? Are you really precommitted? If I know that you know that I know that you know that…

#### The Rules
- Think carefully about the nature of the game and your strategy, this is the most important step!!
- Give me your (nick)name, twitter handle, and a _minimum_ donation of €10
- I give this information to my reliable predictor and prepare the boxes accordingly.
- You will be offered two boxes, a transparent one containing €10 and an opaque one that contains either €100 (if I predict that you will only choose the opaque box), or €0 (if I predict that you will take both boxes).
- You take any money from your chosen box(es) and reflect on what happened. Any money in any remaining boxes goes back to the bank.
#### Byrules
To keep it fun for everyone:

- Don’t donate more than you’re willing to lose.
- Officially you can only play once, but if you ask nicely maybe you can go again.
- Anyone can choose to join the “ethical review board” and I’ll explain my whole spiel but then you can’t play or leak info.
- At the end of the week all will be revealed, and if you wrote down your strategy we can compare notes.

### My reasoning behind all of this

First of all, why “mini byzantine”? I would argue it is the only version that can be played.

First of all. There is no scenario where I trust someone who claims to be an alien in posession of millions of earthly USD and giving it to me. In the real world you’re always dealing with the question of who is this guy, why is he giving me money, and how can he possibly know my choice?

There are only two types of scenarios where you are offered large sums of money: scams and games of chance like a lottery or casino. And both of them make money rather than lose it, by asking for a fee.

You could run this game like a scam or game of chance where there is just a vanishingly small chance there is anything in the opaque box, but that’s not really in the spirit of the problem is it?

Although you could argue that a predictor that reliably predicts two boxes could be argued to be a logical if not ethical solution to the problem. “this is obviously a scam so I should take two boxes” as a sort of self fulfilling prophecy.

So lets impose that this is a zero sum game where I’m neither scamming you out of your money or just giving away money for free, which is the only fair and reasonable way to play.

The introduction of a donation actually meaningfully alters the game under this constraint.

Your donation can be seen as the expected value you place in this game. If you donate 10 euro you obviously place low odds on there being anything in the opaque box, and should logically take two boxes to not lose money. While if you donate 100 euro you obviously expect there to be money in the opaque box that you intend to take. These are the two stable equilibrium.

If you donate 110 you expect to outsmart the predictor which is irrational to singal, and if you donate something inbetween you’re not sure what to expect. So the question is, if your expected value/donation is 50 euro, what should the prediction be?

A reasonable choice in this case depends on the past donations. If people keep donating 50 euro I can’t just keep paying out 100, so we need to look at past donations and payouts compute our account balance and historical expected value. So in order for a logical prediction to be one box, it needs to be higher than or equal to the historical average payout, and the current account balance needs to actually be enough to fill the boxes. That can be easily implemented in an excel spreadsheet, which I did.

I’m really curious if we’ll just get stuck on the 10 euro equilibrium or a trickle of higher donations and mispredictions will lower the expected value low enough for a 100 euro payout to happen. Or will there be people with sufficient trust and money who have come to the same conclusion as me.

### My predictor

I put the following equation in a spreadsheet that checks if your donation is more than the average payout, and the current account balance allows a payout.

`=IF(AND(C2>AVERAGE($E1:E$2), SUM($C$2:C2)-SUM($E1:E$2)>=110), "one box", "two box")`

### Results

Based on the conversations I've had and the number of actual games played (0), we can conclude:

The only winning move is not to play.