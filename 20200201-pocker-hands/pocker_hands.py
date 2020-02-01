class Hand:
    def __init__(self, hand, name='default'):
        self.hand = hand
        self.name = name
        self._face_values = {'T': 10, 'K': 13}
        self._values = self.extract_values()

    def extract_values(self):
        d = self._face_values
        f = lambda x: d[x] if x in d.keys() else int(x)
        result = map(f, [(el[0]) for el in self.hand.split(' ')])
        return result

    def max(self):
        return max(self._values)

    def __gt__(self, other):
        return self.max() > other.max()

    def __eq__(self, other):
        return self.max() == other.max()

    def description(self):
        for k, v in self._face_values.items():
            if self.max() == v:
                return k
        return str(self.max())


def pocker_hands(white, black):
    w = Hand(white, 'White')
    b = Hand(black, 'Black')
    if w == b:
        return 'Tie'
    winner = w if w > b else b
    return '{} wins - high card: {}'.format(winner.name, winner.description())
