import time
from parens import validate, splits

def test_empty_input():
    assert validate("")

def test_simple_paranthesis():
    assert validate("()")

def test_simple_negative():
    assert not validate("(")

def test_simple_paranthesis():
    assert not validate(")(")

def test_double_parenthesis():
    assert not validate("([)]")

def test_double_xxxxx():
    assert not validate("())")

def test_nested_parens():
    assert validate("([])")


def test_foo_bar():
    assert validate("()[]")
    assert validate("{()}[[{}]]")

def test_unbalanced():
    assert not validate("{{)(}}")


def test_generator():
    assert len(list(splits('foobar'))) == 2
    assert ('fo', 'obar') in list(splits('foobar'))
    assert ('foob', 'ar') in list(splits('foobar'))

def test_generator2():
    assert [('fo', 'obar'), ('foob', 'ar')] == list(splits('foobar'))


def test_performance():
    start = time.time()
    validate("(()" * 20)
    end = time.time()
    assert end - start < 1

