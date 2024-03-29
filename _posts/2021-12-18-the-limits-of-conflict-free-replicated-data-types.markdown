---
layout: post
title: The limits of conflict-free replicated data types
categories:
- mosaic
- crdt
---

Imagine you're writing a collaborative application where multiple users are editing a document at the same time.
How do you resolve conflicting edits?

The YOLO solution is last-write-wins, resulting in data loss. The git/CouchDB solution is explicit conflict resolution, by asking the user or using domain-specific logic. The cool kids solution is to use a conflict-free replicated data type (CRDT), promising to never create conflicts in the first place!

A simple to understand CRDT is the add-only set. If two people add different things to a set, you simply add all the things. As long as you never remove things, you can always resolve concurrent edits.

Computer science has given use a whole set of these CRDTs, and people have built nice libraries out of them that promise that as long as you use these data structures you can have a collaborative app that never has conflicts or data loss. Perfect!

Except, the fact that they are conflict free does not mean that the resolution is what the user expected.

Imagine a collaborative drawing program. Lets keep it simple and say the drawing is an add-only set of lines. Perfect, anyone can add lines and there will never be a conflict!

So now Alice and Bob decide to draw a landscape together. Alice starts drawing happy little trees, but Bob suffers an internet glitch and goes offline for a minute. While offline he draws some big snowy mountains. When Bob comes back online, the add-only set neatly merges all their lines without conflict, resulting in a hot mess.

This is not an imaginary scenario. I was showing [Mosaic](https://nyancad.github.io/Mosaic/) to a friend, but their ad blocker blocked synchronization. He drew a nice circuit, unaware of what was already there, and when he disabled his ad blocker, this was the result. Not a single conflict, but not a functional circuit either. (it's a band pass filter and a differential pair, in case you're wondering)

![a band pass filter and differential pair schematic smushed together](/images/conflict.png)

Mosaic is not in fact using a true CRDT, rather it is using CouchDB in a way that avoids conflicts. Each component is its own document, so there aren't conflicts unless two people try to drag the same component at the same time. There is no way to resolve that situation and let both people get what they want. CouchDB has a pretty good section on [designing an application to work with replication](https://docs.couchdb.org/en/stable/best-practices/documents.html#designing-an-application-to-work-with-replication) by the way.

In short, no matter if you use a CRDT or something else, there are situations that cannot be automatically resolved in a way that is generic and does what the user expects. You cannot wish this problem away. To the user last-write-wins data loss and seamlessly-smushed-together data loss are indistinguishable. So how do you handle it?

I think CRDTs are a useful tool, but ultimately suffer from the same "wishing the problem away" attitude as last-write-wins. CouchDB has the right mentality of designing for conflict avoidance, and requiring domain-specific conflict resolution, but it is not a full solution either. As you saw with Mosaic, you can get too good at avoiding conflicts, leading to a result that is unexpected to the end user. You need to think of domain-specific solutions.

In the case of Mosaic the plan is that device-level changes are an explicit conflict that requires user review. If you change the transistor width, and I change the length, that is not something that should be resolved automatically, even though it technically could be. We have both changed the very important W/L ratio, and combining these edits would likely have the wrong result.

At the schematic level, it's hard to tell if some changes are intentional. I don't think you can or should capture overlapping components at the database level, and the best thing to do is offer Electrical Rule Checks(ERC) for likely mistakes, and good edit history to recover from them.