from itertools import zip_longest
import math


def remove_spaces(text):
    return text.replace(' ', '')


def make_into_square(text, size):
    return [ text[x:x + size] for x in range(0, len(text), size) ]


def transpose(matrix):
    return list(''.join(x) for x in zip_longest(*matrix, fillvalue=' '))


def encode(text):
    clean_text = remove_spaces(text)
    size = int(math.sqrt((len(clean_text)))) + 1
    return ' '.join(transpose(make_into_square(clean_text, size)))

