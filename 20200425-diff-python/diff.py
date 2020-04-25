def diff(a, b):
    return a == b


def shorter_longer(a, b):
    return sorted([a, b], key=lambda x: len(x))


def find_substrings(a, substrings, window):
    ln = len(a)
    for start in range(ln):
        end = start + window
        if end <= ln:
            substrings.append(a[start:end])
    if window > 2:
        find_substrings(a, substrings, window-1)
    return substrings


def find_common_substring(a, b):
    if a in b:
        return a
    return ""


def find_biggest_common_substring(a, b):
    s, l = shorter_longer(a, b)
    for ss in [s] + find_substrings(s, [], len(s)-1):
        match = find_common_substring(ss, l)
        if match != "":
            return match
    return ""



