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

class System:
    pass
