import pytest

from bowling import score, spares, strikes


def test_score_0():
    assert 0 == score('')

def test_score_12():
    assert 3 == score('12')

def test_score_11():
    assert 2 == score('11')

def test_score_123456():
    assert 21 == score('123456')

def test_spares_551():
    assert 12 == spares('5/2')

def test_spares_281():
    assert 11 == spares('2/1')

def test_score_28191():
    assert 23 == score('2/1/1')

def test_score_901():
    assert 10 == score('9-1')

def test_strikes_X12():
    assert 13 == strikes('X12')

def test_score_X12():
    assert 13+3 == score('X12')

def test_score_XX1():
    assert 21+11+1 == score('XX1')

def test_heart_break():
    assert 90 == score('9-9-9-9-9-9-9-9-9-9-')

def test_perfect_game():
    assert 270 == score('XXXXXXXXXX--')
