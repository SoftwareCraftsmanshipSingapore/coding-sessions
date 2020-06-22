package com.gildedrose

class Item(val name: String, var sellIn: Int, var quality: Int)

object Item {
  def unapply(item: Item): Option[String] = Option(item.name)
}