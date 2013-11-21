#!/usr/bin/env python
import feedparser
from email.mime.text import MIMEText
from mailbox import mbox, mboxMessage

feed = feedparser.parse("https://www.archlinux.org/feeds/news/")
mail = mbox('/var/spool/mail/news')
ids = set([m['Message-ID'] for m in mail])

for entry in feed.entries:
    if entry.id not in ids:
        message = MIMEText(entry.summary, 'html')
        message['From'] = "ArchNews"
        message['Subject'] = entry.title
        message['Message-ID'] = entry.id
        mail.add(message)

mail.close()
