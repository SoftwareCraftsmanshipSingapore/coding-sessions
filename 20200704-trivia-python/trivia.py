#!/usr/bin/env python

from random import randrange, seed

class QuestionCards:
    CATEGORIES = ['Pop', 'Science', 'Sports', 'Rock']
    def __init__(self):
        self.questions = dict()
        for c in QuestionCards.CATEGORIES:
            self.questions[c] = 0

    def pick(self, place):
        category = QuestionCards.CATEGORIES[place % 4]
        print("The category is %s" % category)
        print("%s Question %i" % (category, self.questions[category]))
        self.questions[category] += 1


class Player:
    def __init__(self, name, questions):
        self.name = name
        self.place = 0
        self.coins = 0
        self.in_penalty_box = False
        self.is_getting_out_of_penalty_box = False
        self.questions = questions

    def roll(self, roll):
        print("%s is the current player" % self.name)
        print("They have rolled a %s" % roll)
        if not self.in_penalty_box:
            self.move(roll)
            self.questions.pick(self.place)
        else:
            if roll % 2 == 1:
                self.get_out_of_penalty_box()
                self.move(roll)
                self.questions.pick(self.place)
            else:
                self.stay_in_penalty_box()

    def move(self, roll):
        self.place = (self.place + roll) % 12
        print(self.name + '\'s new location is ' + str(self.place))

    def correct_answer(self):
        if self.in_penalty_box:
            if self.is_getting_out_of_penalty_box:
                self.on_correct_answer()
                return self.has_won()
            else:
                return False
        else:
            self.on_correct_answer()
            return self.has_won()

    def on_correct_answer(self):
        print('Answer was correct!!!!')
        self.coins += 1
        print(self.name + ' now has ' + str(self.coins) + ' Gold Coins.')

    def wrong_answer(self):
        print('Question was incorrectly answered')
        print(self.name + " was sent to the penalty box")
        self.in_penalty_box = True
        return False

    def get_out_of_penalty_box(self):
        self.is_getting_out_of_penalty_box = True
        print("%s is getting out of the penalty box" % self.name)

    def stay_in_penalty_box(self):
        print("%s is not getting out of the penalty box" % self.name)
        self.is_getting_out_of_penalty_box = False

    def has_won(self):
        return self.coins == 6


class Game:
    def __init__(self):
        self.players = []
        self.n_players = 0
        self.game_round = 0
        self.questions = QuestionCards()

    def add(self, player_name):
        player = Player(player_name, self.questions)
        self.players.append(player)
        print(player.name + " was added")
        self.n_players += 1
        print("They are player number %s" % self.n_players)

    @property
    def current_player(self):
        return self.players[self.game_round % self.n_players]

    def answer(self):
        if self.is_lucky_roll():
            end_game = self.current_player.correct_answer()
        else:
            end_game = self.current_player.wrong_answer()
        return end_game

    def is_lucky_roll(self):
        return randrange(9) != 7

    def next_round(self):
        self.game_round += 1

    def play_round(self):
        self.current_player.roll(randrange(5) + 1)
        end_game = self.answer()
        self.next_round()
        return end_game


import sys

if __name__ == '__main__':
    seed(42)

    if len(sys.argv) > 1:
        game_count = int(sys.argv[1])
    else:
        game_count = 1

    for _ in range(game_count):
        game = Game()

        game.add('Chet')
        game.add('Pat')
        game.add('Sue')

        while True:
            end_game = game.play_round()
            if end_game: break

