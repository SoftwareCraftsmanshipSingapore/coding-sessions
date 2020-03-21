import unittest
import textwrap
from rovers import Rover, Squadron, Station


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

    def test_squadron(self):
        squadron = Squadron([
                    {'pos': (1, 2, 'N'),
                     'cmd': 'LMLMLMLMMX'},
                    {'pos': (3, 3, 'E'),
                     'cmd': 'MMRMMRMRRM'}
                            ])
        squadron.execute()
        result = squadron.get_positions()
        expected = [(1, 3, 'N'), (5, 1, 'E')]
        self.assertEqual(expected, result)

    def test_station(self):
        text = textwrap.dedent("""
        5 5

        1 2 N

        LMLMLMLMM

        3 3 E

        MMRMMRMRRM
        """)
        station = Station()
        instructions = station.parse(text)
        expected = [
                    {'pos': (1, 2, 'N'),
                     'cmd': 'LMLMLMLMM'},
                    {'pos': (3, 3, 'E'),
                     'cmd': 'MMRMMRMRRM'}
                ]
        self.assertEqual(expected, instructions)

    def test_e2e(self):
        text = textwrap.dedent("""
        5 5

        1 2 N

        LMLMLMLMM

        3 3 E

        MMRMMRMRRM
        """)
        expected = textwrap.dedent("""
        1 3 N

        5 1 E
        """)
        station = Station()
        squadron = Squadron(station.parse(text))
        squadron.execute()
        result = squadron.get_positions()
        expected = [(1, 3, 'N'), (5, 1, 'E')]
        self.assertEqual(expected, result)


if __name__ == '__main__':
    unittest.main()
