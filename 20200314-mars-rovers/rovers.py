from itertools import combinations

class Rover:
    def __init__(self, pos):
        self.x, self.y, self.d = pos

    def position(self):
        return self.x, self.y, self.d

    def move(self):
        if self.d == 'S':
            self.y -= 1
        elif self.d == 'E':
            self.x += 1
        elif self.d == 'W':
            self.x -= 1
        else:
            self.y += 1

    def left(self):
        if self.d == 'W':
            self.d = 'S'
        elif self.d == 'S':
            self.d = 'E'
        elif self.d == 'E':
            self.d = 'N'
        else:
            self.d = 'W'

    def right(self):
        for _ in range(3):
            self.left()

    def execute(self, cmds):
        m = {'L': self.left, 'R': self.right, 'M': self.move}
        for cmd in cmds:
            m.get(cmd, self.noops)()

    def noops(self):
        pass


class Squadron:
    def __init__(self, instructions):
        self.instructions = instructions
        self.rovers = []

    def execute(self):
        for instruct in self.instructions:
            r = Rover(instruct['pos'])
            r.execute(instruct['cmd'])
            self.rovers.append(r)

    def get_positions(self):
        return [r.position() for r in self.rovers]


class Station:
    def parse(self, text):
        return [{'pos': self.parse_position(p), 'cmd': c}
            for p, c in self.group_by(2, text.strip().split('\n\n')[1:])]

    def group_by(self, n, lst):
        return list(zip(*[lst[i::n] for i in range(n)]))

    def parse_position(self, txt):
        p = txt.split(' ')
        return (int(p[0]), int(p[1]), p[2])
