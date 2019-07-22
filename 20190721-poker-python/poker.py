#!/usr/bin/python
'''
poker hands
2-9 JQKA
hearts diamonds clubs spades
1 = one-of-a kind
pair
two pair
three of kind
straight
flush
four kind
straight flush
royal flush

hand is N cards, where N >= 5

(major, minor, mini, yamini) = (hand type, tie break value)
one kind = 1, high card, high card suit
two kind = 2, high card, highest suit
two pair = 3, high pair, low pair, suit high pair
three kind = 4, high card
straight = 5, high card, high card suit
flush = 6, high card, high card suit
four of kind = 7, high card
straight flush = 8, high card, high card suit
'''

RANKS="23456789JQKA"
SUITS="hdcs"

def card2rs(card):
    return ( RANKS.index(card[0]), SUITS.index(card[1]) )

assert card2rs("As") == (11, 3)

''' all subhand '''
def score_one_kind(hand):
    r,s = max( hand )
    return (1,r,s, 0)


hand = [ card2rs(x) for x in ['As', 'Kd'] ]
assert score_one_kind(hand) == (1, 11, 3, 0)

def score_pair(hand):
    r,s =  max(hand)
    return (2,r,s, 0)

hand = [ card2rs(x) for x in ['As', 'Ac'] ]
assert score_pair(hand) == (2,11, 3, 0)

def has_pair_order(order):
    for i in range(len(order)-1):
        if order[i][0] == order[i+1][0]:
            return [ order[i], order[i+1] ]
    return None

hand = [ card2rs(x) for x in ['As', 'Ac'] ]
assert has_pair_order(hand) == [ (11,3), (11,2) ]

def has_pair(hand):
    order = has_pair_order(hand)
    for i in range(len(order)-1):
        if order[i][0] == order[i+1][0]:
            return [ order[i], order[i+1] ]
    return None

hand = [ card2rs(x) for x in ['As', 'Ad', 'Kd' ]]
assert has_pair(hand) == [ (11,3), (11,1) ]

def has_two_pair(hand):
    order1 = sorted(hand, reverse=True)
    order2 = sorted(hand)
    pair1 = has_pair_order(order1)
    pair2 = has_pair_order(order2)
    if pair1[0][0] != pair2[0][0]:
        return sorted( [ pair1, pair2], reverse=True )
    return None


def score_two_pair(hand):
    a = [ hand[0][0], hand[0][1] ]
    b = [ hand[1][0], hand[1][1] ]
    return (3,a[0][0],b[0][0],a[0][1])


hand = [card2rs(x) for x in ['As', 'Ad', 'Jd', 'Js'] ]
assert score_two_pair(has_two_pair(hand)) == (3, 11, 8, 3)

funcs = [
        (has_two_pair, score_two_pair),
        (has_pair, score_pair),
        (score_one_kind, score_one_kind) ]


def score(hand):
    ''' returns maj, min, mini '''
    for f,g in funcs:
        a = f(hand)
        if a is not None:
            wtf = g(a)
            return wtf

print("final")
hand = ['As', 'Ad']
hand = [card2rs(x) for x in hand]
assert score(hand) == (2, 11, 3, 0)
print("final2")
hand = ['As', 'Ad', 'Js', 'Jd', '3s']
hand = [card2rs(x) for x in hand]
assert score(hand) == (3,11, 8, 3)
print("yay")
