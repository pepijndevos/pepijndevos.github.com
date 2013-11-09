#!/usr/bin/env python
from mailbox import mbox, mboxMessage

mail = mbox('/var/spool/mail/news')
new = 0
for m in mail:
    if 'O' not in m.get_flags():
        new += 1

mail.close()
exit(new)
