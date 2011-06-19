# 5 minutes Lisp in Python
# Pepijn de Vos <http://pepijndevos.nl>
#
# Inspired by 30 minutes Lisp in Ruby
# http://gist.github.com/562017
# 
# This Lisp does not read or parse anything at all.
# A generator and a Decorator are abused to run sexps.
#
# Usage:
# 
# Define a function decorated with @lisp and start every sexp with yield.
#
# The function names should be strings.
#
# Result is stored in fn name.
#
# Example below:
#

def lisp(fn):
    code = fn()
    val = code.next()
    while True:
        try:
            try:
                newval = getattr(__builtins__, val[0])(*val[1:])
            except AttributeError:
                newval = getattr(val[1], val[0])(*val[2:])

            val = code.send(newval)
        except StopIteration:
            return getattr(val[1], val[0])(*val[2:])

@lisp
def example():
  (yield 'join',
    ", #",
    (yield '__mul__',
      [(yield 'str', i) for i in
        (yield 'range', (yield '__add__', 5, 5))],
      2))

print example