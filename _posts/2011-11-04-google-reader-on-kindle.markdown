---
layout: post
title: Google Reader on Kindle
categories:
- kindle
- googlereader
- rss

---

I recently bought a [Kindle][1], and I love it! It takes some effort to [convert scanned PDF books][2], but other than that, you just email stuff to Amazon, and it gets delivered.

For long articles, I found that [Instapaper][3] has a 'Read Later' bookmarklet, that can be configured to send a digest to my Kindle.

The only hard part was Google Reader, so I thought I'd share that with you. Reader has a 'Send To' feature that can send articles to Kindle, but you have to go trough all the articles manually.

We'll have to involve an extra step, called [Instascriber][4]. Instascriber lets you automatically send up to three feeds to Instapaper. Not enough for all my feeds, but luckily Reader has an RSS feed hidden deep down.

The broad approach is to collect your feeds in a *public* place in Google reader, so you can put the RSS feed in Instascriber, which submits them to Instapaper, which in turn emails them to Amazon, which then finally delivers the issue on my Kindle.

Finding a public RSS feed in Reader is a bit tricky. Information I found about public folders and tags seemed outdated, with the new interface. What you need to do is, go to 'Browse for stuff', and create a bundle there.

![image of google reader][5]

When you're done, you need to figure out how to get the feed. I did this by clicking 'Add a link', to get to the public page. Depending on your browser, you might already see the RSS icon there. For FireFox, it can be found by pressing CMD+I and going to the 'Feeds' tab.

For reference, this is what my feed looks like <http://www.google.com/reader/public/atom/user%2F09723460687514136094%2Fbundle%2Fkindle>

From here on, it's standard form-filling and do-as-you-are-told business.

First register at Instapaper, and set up Kindle delivery [according to their instructions][6]

Finally, register at Instascriber, enter the Reader feed, and enjoy reading!

[1]: http://www.amazon.com/dp/B0051QYGXA?tag=wishcodi-20
[2]: http://willus.com/k2pdfopt/
[3]: http://www.instapaper.com
[4]: http://www.instascriber.com/
[5]: /images/googlereader.png
[6]: http://www.instapaper.com/user/kindle