package dev.jans


object Items {
  sealed trait ItemType
  case object Metals extends  ItemType
  case object Medicines extends  ItemType
  case object Food extends  ItemType

  type Inventory = Map[ItemType,Int]
  type ShopPrices = Map[ItemType,Int]

  def getItemCount(inventory: Inventory)(itemType: ItemType): Int =
    inventory.getOrElse(itemType,0)

  def inventoryToString(i:Inventory): String =
    s"Metals :${getItemCount(i)(Metals)}, Medicines :${getItemCount(i)(Medicines)}, Food :${getItemCount(i)(Food)} "

  val shopPrices: Map[ItemType, Int] = Map(Metals->10, Medicines->50, Food->5)

}
