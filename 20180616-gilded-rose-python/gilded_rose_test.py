from gilded_rose import GildedRose, Item


def validate(item):
    assert 0 <= item.quality <= 50


def test_aged_brie_the_older_the_better():
    i = Item("Aged Brie", 123, 42)
    validate(i)
    GildedRose([i]).update_quality()
    validate(i)
    assert i.quality > 42, "Aged Brie increases in quality the older it gets"


def test_aged_brie_exact_quality_increase():
    i = Item("Aged Brie", 123, 42)
    validate(i)
    GildedRose([i]).update_quality()
    validate(i)
    assert i.quality == 43


def test_aged_brie_as_good_as_it_gets():
    i = Item("Aged Brie", 123, 50)
    validate(i)
    GildedRose([i]).update_quality()
    validate(i)
    assert i.quality == 50


def test_normal_item_quality_decrease():
    i = Item("Foo", 123, 42)
    validate(i)
    GildedRose([i]).update_quality()
    validate(i)
    assert i.quality == 41

def test_conjured_item_quality_decrease():
    i = Item("Conjured", 123, 42)
    validate(i)
    GildedRose([i]).update_quality()
    validate(i)
    assert i.quality == 40



def test_normal_item_at_zero():
    i = Item("Foo", 123, 0)
    validate(i)
    GildedRose([i]).update_quality()
    validate(i)
    assert not i.quality


def test_normal_item_quality_decrease_after_sell_by_date():
    i = Item("Foo", 0, 42)
    validate(i)
    GildedRose([i]).update_quality()
    validate(i)
    assert i.quality == 40


def test_sulfuras_quality_doesnot_change():
    i = Item("Sulfuras, Hand of Ragnaros", 123, 42)
    validate(i)
    GildedRose([i]).update_quality()
    validate(i)
    assert i.quality == 42

BACKSTAGE= "Backstage passes to a TAFKAL80ETC concert"

def test_backstage_quality_increases():
    i = Item(BACKSTAGE, 11, 42)
    validate(i)
    GildedRose([i]).update_quality()
    validate(i)
    assert i.quality == 43

def test_backstage_quality_increases_after_ten_days():
    i = Item(BACKSTAGE, 10, 42)
    validate(i)
    GildedRose([i]).update_quality()
    validate(i)
    assert i.quality == 44

def test_backstage_quality_increases_after_five_days():
    i = Item(BACKSTAGE, 5, 42)
    validate(i)
    GildedRose([i]).update_quality()
    validate(i)
    assert i.quality == 45


def gen_value(i, days=99):
    validate(i)
    for _ in range(days):
        GildedRose([i]).update_quality()
        validate(i)
        yield i.quality


def test_backstage_schedule():
    i = Item(BACKSTAGE, 12, 0)
    assert list(gen_value(i, 12 + 2)) == [1, 2, 4, 6, 8, 10, 12, 15, 18, 21, 24, 27, 0, 0]


def test_normal_schedule():
    i = Item("foo", 10, 5)
    assert list(gen_value(i, 5 + 2)) == [4, 3, 2, 1, 0, 0, 0]


def test_normal_schedule_past_expiration():
    i = Item("foo", 10, 15)
    assert list(gen_value(i, 10 + 3)) == [14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 3, 1, 0]


def test_brie_schedule():
    i = Item("Aged Brie", 10, 45)
    assert list(gen_value(i, 10)) == [46, 47, 48, 49, 50, 50, 50, 50, 50, 50]


def test_aged_brie_past_expiration():
    i = Item("Aged Brie", 0, 42)
    GildedRose([i]).update_quality()
    assert i.quality == 44
