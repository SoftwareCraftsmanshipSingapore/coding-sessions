def is_vowel(char):
    return char in 'aeiouy'


def count_syllables(word):
    count = 0
    prev_ch = 'x'
    for ch in word.lower():
        if is_vowel(ch) and not is_vowel(prev_ch):
            count += 1
        prev_ch = ch
    return count


def verify(text):
    if '/' in text:
        haiku_lines = text.split('/')
    else:
        haiku_lines = text.split()
    syllables = []
    for line in haiku_lines:
        syllables.append(count_syllables(line))
    ans = 'Yes' if syllables == [5,7,5] else 'No'
    return ",".join(map(str, syllables)) + ',' + ans


###### Tests

def test_verify_single_haiku_split_by_spaces():
    assert "1,0,1,No" == verify("a b a")


def test_verify_happy_purple_frog():
    assert "5,7,5,Yes" == verify("happy purple frog/eating bugs in the marshes/get indigestion")


def test_verify_computer_programs():
    assert "5,8,5,No" == verify("computer programs/the bugs try to eat my code/i will not let them")


def test_count_syllables_one():
    assert 1 == count_syllables('za')


def test_count_syllables_two():
    assert 2 == count_syllables('aza')


def test_count_syllables_contiguous():
    assert 2 == count_syllables('aiza')


def test_count_syllables_contiguous_with_mixed_case():
    assert 2 == count_syllables('AYzoi')

