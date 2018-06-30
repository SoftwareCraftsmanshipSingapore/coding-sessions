def convert_char(ch):
    if ch == 'X':
        return 10
    if ch == '-':
        return 0
    return int(ch)


def strikes(pins):
    if pins[0] == 'X':
        return 10 + sum([convert_char(p) for p in pins[1:]])
    return 0


def spares(pins):
    if pins[1] == '/':
        return 10 + int(pins[2])
    return 0


def score(pins):
    total = 0
    previous = ''
    for idx, pin in enumerate(pins):
        if pin == '/':
            total += spares(pins[idx-1:idx+2])
            total -= int(previous)
        elif pin == 'X' and idx < 10:
            total += strikes(pins[idx:idx+3])
        elif pin == '-':
            continue
        else:
            total += convert_char(pin)
        previous = pin
    return total
