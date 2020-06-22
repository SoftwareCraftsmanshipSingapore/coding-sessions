package com.gildedrose

class GildedRose(val items: Array[Item]) {
  import GildedRose._

  def updateQuality() {
    items.foreach {
      case i@Item("Sulfuras, Hand of Ragnaros")                => i
      case i@Item("Aged Brie")                                 => i.ageItem().incQuality().incQuality(_.expired)
      case i@Item("Backstage passes to a TAFKAL80ETC concert") => i.ageItem().incQuality().incQuality(_.sellIn < 10).incQuality(_.sellIn < 5).zeroQuality(_.expired)
      case i                                                   => i.ageItem().decQuality().decQuality(_.expired)
    }
  }
}

object GildedRose {
  implicit class ItemOps(val item: Item) extends AnyVal {
    def decQuality(guard: Item => Boolean = _ => true):Item = {
      if (item.quality > 0 && guard(item)) item.quality -= 1
      item
    }
    def incQuality(guard: Item => Boolean = _ => true):Item = {
      if (item.quality < 50 && guard(item)) item.quality += 1
      item
    }
    def expired: Boolean = item.sellIn < 0

    def zeroQuality(guard: Item => Boolean = _ => true):Item = {
      if (guard(item)) item.quality = 0
      item
    }

    def ageItem(): Item = {
      item.sellIn -= 1
      item
    }
  }
}