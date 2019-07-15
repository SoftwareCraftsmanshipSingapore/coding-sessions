#!/usr/bin/python

'''
path(src, dst, history)
poss(word) -> generator of possible next words
words = list of words of length N
'''

import string, sys
sys.setrecursionlimit(100000)
LEN = 3

dictionary = '/usr/share/dict/words' 

def Testwords(length):
    words = open(dictionary).readlines()
    words = [ w.strip() for w in words if w[0] not in string.ascii_uppercase ]
    words = [ w for w in words if len(w) == length ]
    return words

assert all(len(x) == 4 for x in Testwords(4) )

def score(src, dst):
    ''' levenshtein diff '''
    res = len(src)
    for i in range(len(src)):
        if src[i] == dst[i]:
            res -= 1
    return res 

assert score('dig', 'dug') == 1
def DRposs(src, words):
    for w in words:
        if score(src, w) == 1:
            yield w

def Rposs(src, words):
    for i in range(len(src)):
        for j in string.ascii_lowercase:
            if j == src[i]:
                continue
            w = src[:i] + j + src[i+1:]
            if w in words:
                yield w

words ='''dog dug cog dit'''.split()


def poss(src, dst, words):
    return sorted( list(Rposs(src, words)), key= lambda x: score(dst, x) )

def printit(x):
    print [ y for y in x ]

words ='''dog dug cog dit'''.split()
assert poss('dig', 'cog', words) == '''dog dug dit'''.split()

words ='''dog cog dot cot cat cut dug dig'''.split()
assert set(poss('dog', 'dug', words)) == set(['dot', 'dug', 'dig', 'cog'])

cache = dict()

def cache_poss(src, dst, words):
    res = cache.get( (src,dst))
    if res is not None:
        return res
    res = poss(src, dst, words)
    cache[ (src,dst) ] = res
    return res

deadend_cache = {}

def path(src, dst, words, history, max_len=10):
    if len(history) > max_len:
        return None
    if src == dst:
        return history+[src]
    if src in history:
        return None
    for p in cache_poss(src, dst, words):
       res = deadend_path(p, dst, words, history+[src], max_len)
       if res is not None:
           return res
    return None

def deadend_path(src, dst, words, history, max_len=10):
    is_deadend = deadend_cache.get((src, dst), None)
    if is_deadend:
        return None
    result = path(src, dst, words, history, max_len)
    deadend_cache[(src, dst)] = result is None
    return result

words ='''dog cog dug'''.split()
assert path('dog', 'dug', words, []) == ['dog', 'dug']
assert path('dog', 'dup', words, []) == None
words ='''dog dot cot cat dug dig'''.split()
assert path('dog', 'cot', words, []) == ['dog', 'dot', 'cot']

#drpos = 420ms
#print path('bough', 'coral', Testwords(5), [], max_len=15)
# we cannot do this...
words =Testwords(6)
print len(words)
print deadend_path('trough', 'column', words, [], max_len=len(words))
print deadend_path('winter', 'summer', Testwords(6), [], max_len=15)


