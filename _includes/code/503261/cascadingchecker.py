from twisted.cred.checkers import ICredentialsChecker
from twisted.cred import error, credentials
from zope.interface import implements
from collections import deque

class CascadingChecker:
    """
    Check multiple checkers untill one succeeds.
    Else raise UnauthorizedLogin.
    """

    implements(ICredentialsChecker)
    credentialInterfaces = set()
    
    def __init__(self):
        self.checkers = []
        self.checked = []
    
    def registerChecker(self, checker):
        self.checkers.append(checker)
        self.credentialInterfaces.update(checker.credentialInterfaces)
    
    def _requestAvatarId(self, err, queue, credentials):
        try:
            ch = queue.popleft()
        except IndexError:
            raise error.UnauthorizedLogin()
        
        d = ch.requestAvatarId(credentials)
        return d.addErrback(self._requestAvatarId, queue, credentials)
    
    requestAvatarId = lambda self, credentials: self._requestAvatarId(None, deque(self.checkers), credentials)