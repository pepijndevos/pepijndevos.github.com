"""
An example pop3 server
"""

from twisted.application import internet, service
from twisted.cred.portal import Portal, IRealm
from twisted.internet.protocol import ServerFactory
from twisted.mail import pop3
from twisted.mail.pop3 import IMailbox
from twisted.cred.checkers import InMemoryUsernamePasswordDatabaseDontUse
from zope.interface import implements
from itertools import repeat
from hashlib import md5
from StringIO import StringIO

class SimpleMailbox:
    implements(IMailbox)

    def __init__(self):
        message = """From: me
To: you
Subject: A test mail

Hello world!"""
        self.messages = [m for m in repeat(message, 20)]


    def listMessages(self, index=None):
        if index != None:
            return len(self.messages[index])
        else:
            return [len(m) for m in self.messages]

    def getMessage(self, index):
        return StringIO(self.messages[index])

    def getUidl(self, index):
        return md5(self.messages[index]).hexdigest()

    def deleteMessage(self, index):
        pass

    def undeleteMessages(self):
        pass

    def sync(self):
        pass


class SimpleRealm:
    implements(IRealm)

    def requestAvatar(self, avatarId, mind, *interfaces):
        if IMailbox in interfaces:
            return IMailbox, SimpleMailbox(), lambda: None
        else:
            raise NotImplementedError()

portal = Portal(SimpleRealm())

checker = InMemoryUsernamePasswordDatabaseDontUse()
checker.addUser("guest", "password")
portal.registerChecker(checker)

application = service.Application("example pop3 server")

f = ServerFactory()
f.protocol = pop3.POP3
f.protocol.portal = portal
internet.TCPServer(1230, f).setServiceParent(application)
