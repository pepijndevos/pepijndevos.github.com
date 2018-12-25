---
layout: post
title: Visualising your WhatsApp conversations
categories:
- python
- pandas
---

I was curious how much I chat with my friends, so I decided to find out. It's pretty easy and fun, I can promise.

My first thought was to go after the message database, but it turns out this is encrypted with a key that can only be obtained by rooting your device or performing some downgrade attack. Not sure what the security model is here, as the key is obviously on my device and on their servers. (Else you could not restore a backup on another phone)

Instead I went for the chat export feature that is hidden in Menu > Settings > Chats > Chat history > Export chat. This is per chat, so a bit tedious. But I have a rough idea who I talk to a lot, so it's not too bad.

From there you can do whatever you want. Look at file sizes, grep for random things, or write a simple Python script. I stole a regex from SO, fixed it for Python, put everything in a Pandas DataFrame, and from there you can just play with the df and then call plot on it.

Most of what I did so far is count the messages/characters per month. Very interesting to see how things change over time and in response to real life events. Picture omitted as a reminder how much you can tell about someone by meta-data alone.

{% highlight python %}
import re
import sys
import pandas as pd
import matplotlib.pyplot as plt

regex = """(?P<datetime>\d{1,2}\/\d{1,2}\/\d{1,4}, \d{1,2}:\d{1,2}( (?i)[ap]m)*) - (?P<name>.*(?::\s*\w+)*|[\w\s]+?)(?:\s+(?P<action>joined|left|was removed|changed the (?:subject to "\w+"|group's icon))|:\s(?P<message>(?:.+|\n(?!\d{1,2}\/\d{1,2}\/\d{1,4}, \d{1,2}:\d{1,2}( (?i)[ap]m)*))+))"""

files = sys.argv[1:]

for fname in files:
    with open(fname) as f:
        text = f.read()
    matches = re.findall(regex, text)
    messages = []
    for match in matches:
        messages.append(match[::2])

    df = pd.DataFrame(messages, columns=['datetime', 'name', 'message'])
    df.datetime = pd.to_datetime(df.datetime, dayfirst=True)
    df.set_index('datetime', inplace=True)
    lengths = df.message.str.len()
    monthly = lengths.groupby(pd.Grouper(freq='M')).sum()
    #monthly = lengths.groupby(pd.Grouper(freq='D')).count().rolling('30d', min_periods=1).sum()
    #plt.figure()
    #ax = monthly.plot(title=fname[19:-4])
    ax = monthly.plot()

ax.legend([fname[19:-4] for fname in files])
plt.ylabel("bytes/month")
#plt.ylabel("messages/month")
plt.show()
{% endhighlight %}