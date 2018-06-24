# data representation
# ["xxx", "yyy", "zzz"]

CONTINUE = 0
WIN_X = 1
WIN_O = 2
DRAW = 3


def transpose(board):
    return [ ''.join(x) for x in zip(*board) ]


def eval_horizontal(board):
    if any(x == "xxx" for x in board):
        return WIN_X
    if any(x == "ooo" for x in board):
        return WIN_O
    return CONTINUE


def eval_vertical(board):
    return eval_horizontal(transpose(board))


def eval_diagonal(board):
    for x,y,z in [ (0,1,2), (2,1,0) ]:
        selected = ''.join([board[0][x],board[1][y],board[2][z]])
        if selected == 'xxx':
            return WIN_X
        if selected == 'ooo':
            return WIN_O
    return CONTINUE


def eval_draw(board):
    if all(x in 'ox' for x in ''.join(board)):
        return DRAW
    return CONTINUE


def eval_board(board):
    return eval_horizontal(board) or eval_vertical(board) or eval_diagonal(board) or eval_draw(board)


# tests
def test_continue():
    assert eval_board(["xoo", "...", "..."]) == CONTINUE


def test_x_win_horizontal():
    assert eval_board(["...", "xxx", "..."]) == WIN_X


def test_o_win_horizontal():
    assert eval_board(["...", "ooo", "..."]) == WIN_O


def test_x_win_vertical():
    assert eval_board("x.. x.. x..".split()) == WIN_X


def test_x_win_diagonal():
    assert eval_board("x.. .x. ..x".split()) == WIN_X


def test_o_win_other_diagonal():
    assert eval_board("..o .o. o..".split()) == WIN_O


def test_draw():
    assert eval_board("xxo oox xox".split()) == DRAW
