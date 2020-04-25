import unittest
import textwrap
from diff import *


class TestDiff(unittest.TestCase):

    def test_find_common1(self):
        a = "ab"
        b = "abc"
        self.assertEqual(find_biggest_common_substring(a, b), "ab")

    def test_find_common2(self):
        a = "abc"
        b = "ab"
        self.assertEqual(find_biggest_common_substring(a, b), "ab")

    def test_find_common3(self):
        a = "abc"
        b = "bcde"
        self.assertEqual(find_biggest_common_substring(a, b), "bc")

    def test_find_common4(self):
        a = "abcd"
        b = "cdefg"
        self.assertEqual(find_biggest_common_substring(a, b), "cd")

    def test_find_common5(self):
        a = "abcde"
        b = "defghi"
        self.assertEqual(find_biggest_common_substring(a, b), "de")

    def test_find_substrings(self):
        ss = find_substrings("abcde", [], 4)
        self.assertEqual(ss, ["abcd", "bcde", "abc", "bcd", "cde", "ab", "bc", "cd", "de"])

    def test_shorter_longer(self):
        s, l = shorter_longer("ab", "a")
        self.assertEqual([s, l], ["a", "ab"])

    def xx_test_stand_alone_function(self):
        first = "foo abxxxab"
        second = "abyyyab"
        expected = textwrap.dedent("""
        foo abxxxx++++ab
        ----ab----yyyyab
        """)

        first = "abyyyab"
        second = "foo abxxxab"
        expected = textwrap.dedent("""
        ++++ab++++yyyyab
        foo abxxxx----ab
        """)

        result = diff(first, second)
        self.assertEqual(result, expected)



if __name__ == '__main__':
    unittest.main()
