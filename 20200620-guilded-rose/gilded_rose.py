# -*- coding: utf-8 -*-
CONCERT = "Backstage passes to a TAFKAL80ETC concert"
SULFURAS = "Sulfuras, Hand of Ragnaros"
CHEESE = "Aged Brie"


def increase_quality(quality):
    return quality + 1 if (quality < 50) else quality


def decrease_quality(item):
    return item.quality - 1 if (item.quality > 0 and item.name != SULFURAS) else item.quality


def is_normal(item):
    return item.name != CHEESE and item.name != CONCERT


def is_special(item):
    return item.name in (CHEESE, CONCERT)


def increase_concert_quality(item):
    quality = item.quality
    if item.name == CONCERT:
        if item.sell_in < 11:
            quality = increase_quality(quality)
        if item.sell_in < 6:
            quality = increase_quality(quality)
    return quality


class GildedRose(object):

    def __init__(self, items):
        self.items = items

    def update_quality(self):
        for item in self.items:
            if not is_special(item):
                item.quality = decrease_quality(item)
            else:
                if item.quality < 50:
                    item.quality = item.quality + 1
                    item.quality = increase_concert_quality(item)
            if item.name != SULFURAS:
                item.sell_in = item.sell_in - 1
            if item.sell_in < 0:
                if item.name != CHEESE:
                    if item.name != CONCERT:
                        item.quality = decrease_quality(item)
                    else:
                        item.quality = 0
                else:
                    item.quality = increase_quality(item.quality)


class Item:
    def __init__(self, name, sell_in, quality):
        self.name = name
        self.sell_in = sell_in
        self.quality = quality

    def __repr__(self):
        return "%s, %s, %s" % (self.name, self.sell_in, self.quality)
