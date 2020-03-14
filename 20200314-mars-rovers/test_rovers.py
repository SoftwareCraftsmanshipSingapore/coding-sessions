import unittest
from rovers import Rover, System


class TestMarsRover(unittest.TestCase):

    def test_rover_init(self):
        r = Rover((1, 2, 'N'))
        self.assertEqual((1, 2, 'N'), r.position())

    def test_rover_move(self):
        start_end = [ 
                [(1, 2, 'N'), (1, 3, 'N')],
                [(1, 2, 'S'), (1, 1, 'S')],
                [(1, 2, 'E'), (2, 2, 'E')],
                [(1, 2, 'W'), (0, 2, 'W')],
                ]
        for start, end in start_end:
            r = Rover(start)
            r.move()
            self.assertEqual(end, r.position())

    def test_rover_left(self):
        start_end = [ 
                [(1, 2, 'N'), (1, 2, 'W')],
                [(1, 2, 'W'), (1, 2, 'S')],
                [(1, 2, 'S'), (1, 2, 'E')],
                [(1, 2, 'E'), (1, 2, 'N')],
                ]
        for start, end in start_end:
            r = Rover(start)
            r.left()
            self.assertEqual(end, r.position())

    def test_rover_right(self):
        start_end = [ 
                [(1, 2, 'N'), (1, 2, 'W')],
                [(1, 2, 'W'), (1, 2, 'S')],
                [(1, 2, 'S'), (1, 2, 'E')],
                [(1, 2, 'E'), (1, 2, 'N')],
                ]
        for end, start in start_end:
            r = Rover(start)
            r.right()
            self.assertEqual(end, r.position())

    def test_rover_execute(self):
        r = Rover((1, 2, 'N'))
        r.execute('LMLMLMLMMX')
        self.assertEqual((1, 3, 'N'), r.position())

    def xxtest_e2e(self):
        test_input = """
5 5

1 2 N

LMLMLMLMM

3 3 E
"""

        expected = """
1 3 N

5 1 E
"""
        System.send_cmd(test_input)
        result = System.whatsgoinging()
        self.assertEqual(expected, result)


if __name__ == '__main__':
    unittest.main()
