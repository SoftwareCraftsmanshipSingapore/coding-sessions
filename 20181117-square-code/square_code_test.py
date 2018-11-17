import pytest
from square_code import *


def test_remove_space():
    assert 'abcd' == remove_spaces('ab cd')

def test_remove_spaces():
    assert 'abcdef' == remove_spaces(' ab  cd ef')

def test_trivial_make_into_square():
    assert ['ab', 'c'] == make_into_square('abc', 2)

def test_less_trivial_make_into_square():
    assert ['ab', 'cd'] == make_into_square('abcd', 2)

def test_trivial_make_into_square4():
    assert ['123', '456', '789', '0'] == make_into_square('1234567890', 3)

def test_transpose_square():
    assert ['ac', 'bd'] == transpose(['ab', 'cd'])

def test_transpose_3x3():
    assert ['adg', 'beh', 'cf '] == transpose(['abc', 'def', 'gh'])

def test_haveaniceday():
    assert "hae and via ecy" == encode("have a nice day")

def test_feedthedog():
    assert "fto ehg ee  dd " == encode("feedthedog")

def test_the_ultimate():
    expected = "imtgdvs fearwer mayoogo anouuio ntnnlvt wttddes aohghn  sseoau "
    input = "if man was meant to stay on the ground god would have given us roots"
    assert 54 == len(remove_spaces(input))
    assert expected == encode(input)


