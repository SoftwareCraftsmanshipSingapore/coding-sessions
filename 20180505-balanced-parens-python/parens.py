"""Validates a sequence of parens of different kinds as valid
We do this with recursion as follows:
  - if input is empty then it is valid
  - if the first and the last chars make a pair then we consider the remainder of the input

Examples:
  (foo) is valid if foo is valid:
    ([])
  foobar is valid if both foo and bar is valid:
    ()[] is valid


we should test the following
"""

PAIRS = ["()", "[]", "{}"]


def splits(st):
    for i in range(2, len(st)-1, 2):
        yield st[0:i], st[i:len(st)]

def memoize(f):
    f.cache = {}
    def inner(x):
        if x in f.cache:
            return f.cache[x]
        else:
            tmp = f(x)
            f.cache[x] = tmp
            return tmp   
    return inner

@memoize
def validate(st):
    if not st:
        return True
    perimeter = st[0] + st[-1]
    inside = st[1:-1]
    if perimeter in PAIRS and validate(inside):
        return True
    for left, right in splits(st):
        if validate(left) and validate(right):
            return True
