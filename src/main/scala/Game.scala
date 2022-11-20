package dev.jans

import Items.{Inventory, ItemType, ShopPrices, getItemCount, inventoryToString}


object Game {

  val startingInv: Map[Items.ItemType, Int] = Map(Items.Metals -> 10)

  case class Player(money: Int = 1000, inventory: Items.Inventory = startingInv) {
    override def toString() = "Player has: " + money + " credits. " + inventoryToString(inventory)
  }

  case class Model(turn: Int = 1, player: Player = Player()) {
    override def toString() = "Turn: " + turn + ", " + player
  }

  def progressTime(model: Model)(t: Int): Model = model.copy(turn = model.turn + t)

  def trade(model: Model)(itemType: ItemType, count: Int, shopPrices: ShopPrices): Model = {
    val player = model.player
    val inventory = player.inventory
    val cost = shopPrices.getOrElse(itemType, 0) * count
    if (cost > player.money) model
    else if (count > 0 || -count <= (getItemCount(inventory)(itemType))) {
      val newInventory: Inventory = inventory.map {
        case (itemType, c) => (itemType, c + count)
      }
      val newMoney = player.money - cost
      val player_ = player.copy(money = newMoney, inventory = newInventory)
      model.copy(player = player_)
    }
    else {
      model
    }
  }
}
