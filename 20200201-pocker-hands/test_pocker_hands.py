import unittest
from pocker_hands import Hand, pocker_hands

test_cases = (
    ('white hand', 'black hand', 'result'),
    ('2H 3D 4S 5C 7D', '2C 3H 4S 5C 6H', 'White wins - high card: 7'),
    ('2H 3D 4S 5C 7D', '2C 3H 4S 5C 8H', 'Black wins - high card: 8'),
    ('2H 3D 4S 5C KD', '2C 3H 4S 5C 6H', 'White wins - high card: K'),
    ('2H 3D 4S 5C 9D', '2C 3H 4S 5C TH', 'Black wins - high card: T'),
)


class TestForPockerHand(unittest.TestCase):

    def test_high_card(self):
        for idx, a_test in enumerate(test_cases[1:]):
            white, black, expected = a_test
            result = pocker_hands(white, black)
            error = "case {} failed. Result: {}".format(idx + 1, result)
            self.assertEqual(result, expected, error)

    def test_high_card_tie(self):
        self.assertEqual(pocker_hands('2H 3D', '2H 3D'), 'Tie')

    def test_extract_values(self):
        h = Hand('2H 3D 4S')
        result = h.extract_values()
        expected = [2, 3, 4]
        self.assertEqual(result, expected)

    def test_hand_is_bigger(self):
        h1 = Hand('2H 3D 4S 5C 8D')
        h2 = Hand('2H 3D 4S 5C 7D')
        self.assertTrue(h1 > h2)

    def xxx_test_detect_pattern(self):
        self.assertEqual(pocker_pattern(hand='2H 2D 4S 5C 7D'), 'pair')


if __name__ == '__main__':
    unittest.main()


'''A poker deck contains 52 cards - each card has a suit which
is one of clubs, diamonds, hearts, or spades
(denoted C, D, H, and S in the input data).

Each card also has a value which is one of
2, 3, 4, 5, 6, 7, 8, 9, 10, jack, queen, king, ace
(denoted 2, 3, 4, 5, 6, 7, 8, 9, T, J, Q, K, A).

For scoring purposes, the suits are unordered while the
values are ordered as given above, with 2 being the lowest
and ace the highest value.

A poker hand consists of 5 cards dealt from the deck. Poker
hands are ranked by the following partial order from lowest
to highest.

High Card: Hands which do not fit any higher category are
ranked by the value of their highest card. If the highest
cards have the same value, the hands are ranked by the next
highest, and so on.

Pair: 2 of the 5 cards in the hand have the same value.
Hands which both contain a pair are ranked by the value of
the cards forming the pair. If these values are the same,
the hands are ranked by the values of the cards not
forming the pair, in decreasing order.

Two Pairs: The hand contains 2 different pairs. Hands
which both contain 2 pairs are ranked by the value of
their highest pair. Hands with the same highest pair
are ranked by the value of their other pair. If these
values are the same the hands are ranked by the value
of the remaining card.

Three of a Kind: Three of the cards in the hand have the
same value. Hands which both contain three of a kind are
ranked by the value of the 3 cards.

Straight: Hand contains 5 cards with consecutive values.
Hands which both contain a straight are ranked by their
highest card.

Flush: Hand contains 5 cards of the same suit. Hands which
are both flushes are ranked using the rules for High Card.

Full House: 3 cards of the same value, with the remaining 2
cards forming a pair. Ranked by the value of the 3 cards.

Four of a kind: 4 cards with the same value. Ranked by the
value of the 4 cards.

Straight flush: 5 cards of the same suit with consecutive
values. Ranked by the highest card in the hand.

Your job is to rank pairs of poker hands and to indicate
which, if either, has a higher rank.

Examples:
Input: Black: 2H 3D 5S 9C KD White: 2C 3H 4S 8C AH
Output: White wins - high card: Ace

Input: Black: 2H 4S 4C 2D 4H White: 2S 8S AS QS 3S
Output: Black wins - full house

Input: Black: 2H 3D 5S 9C KD White: 2C 3H 4S 8C KH
Output: Black wins - high card: 9

Input: Black: 2H 3D 5S 9C KD White: 2D 3H 5C 9S KH
Output: Tie
'''