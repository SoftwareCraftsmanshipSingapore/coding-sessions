# -*- coding: utf-8 -*-

class GildedRose(object):

    def __init__(self, items):
        self.items = items

    def update_quality(self):
        for item in self.items:
            update(item)


class Item:
    def __init__(self, name, sell_in, quality):
        self.name = name
        self.sell_in = sell_in
        self.quality = quality

    def __repr__(self):
        return "%s, %s, %s" % (self.name, self.sell_in, self.quality)


def update_before(self):
    if self.brie:
        self.quality += 1


def update(self):
    self.conjured = self.name == "Conjured"
    self.brie = self.name == "Aged Brie"
    self.timeless = self.name == "Sulfuras, Hand of Ragnaros"
    self.backstage = self.name == "Backstage passes to a TAFKAL80ETC concert"
    self.normal = not self.brie and not self.backstage and not self.timeless and not self.conjured

    update_before(self)

    if not self.timeless:
        self.sell_in -= 1

    update_backstage(self)

    if self.sell_in < 0:
        update_after_expiration(self)

    update_normal(self)
    update_conjured(self)
    self.quality = min(50, max(0, self.quality))


def update_normal(self):
    if self.normal:
        by = 2 if self.sell_in < 0 else 1
        self.quality -= by


def update_conjured(self):
    if self.conjured:
        by = 4 if self.sell_in < 0 else 2
        self.quality -= by


def update_after_expiration(self):
    if self.brie:
        self.quality += 1


def update_backstage(self):
    if self.backstage:
        by = 3 if self.sell_in < 5 else 2 if self.sell_in < 10 else 1
        self.quality += by
        if self.sell_in < 0:
            self.quality = 0
