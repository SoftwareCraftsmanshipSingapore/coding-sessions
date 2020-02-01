package shop

package object model {
  type OrderNumber = Int
  type Money       = BigDecimal
  type Products    = Seq[Product]

  trait Product {
    def price : Money
  }

  case class Coffee(kind: String, size: String, override val price: Money) extends Product

  case class Order(number: OrderNumber, products: Products) {
    def totalPrice : Money = products.foldLeft(BigDecimal(0))(_ + _.price)
  }

  case class Receipt(orderNumber: OrderNumber, money: Money)

}
