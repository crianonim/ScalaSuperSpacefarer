package dev.jans

import Items.{Inventory, ItemType, Shop, ShopPrices, exampleShop, getItemCount, inventoryToString}


object Game {

  val startingInv: Map[Items.ItemType, Int] = Map(Items.Metals -> 10, Items.Medicines -> 0, Items.Food -> 0)

  case class Player(money: Int = 1000, inventory: Items.Inventory = startingInv) {
    override def toString() = "Player has: " + money + " credits. " + inventoryToString(inventory)
  }

  case class Model(turn: Int = 1, player: Player = Player(), shop: Shop = exampleShop) {
    override def toString() = "Turn: " + turn + ", " + player
  }

  def progressTime(model: Model)(t: Int): Model = model.copy(turn = model.turn + t)

  def moveItem(from: Inventory, to: Inventory, itemType: ItemType, count: Int): (Inventory, Inventory) = {
    val from_ = from.map {
      case (it, c) => if (it == itemType) (itemType, c - count) else (it, c)
    }
    val to_ = to.map {
      case (it, c) => if (it == itemType) (itemType, c + count) else (it, c)
    }
    (from_, to_)
  }

  sealed trait TradeError

  case object ZeroTradeCount extends TradeError

  case object PlayerNotEnoughMoney extends TradeError

  case object ShopNotEnoughMoney extends TradeError

  case object PlayerNotEnoughInventory extends TradeError

  case object ShopNotEnoughInventory extends TradeError

  def trade_(player: Player, shop: Shop, itemType: ItemType, count: Int): Either[TradeError, (Player, Shop)] = {

    val cost = shop.shopPrices.getOrElse(itemType, 0) * count
    if (count == 0) return Left(ZeroTradeCount)

    if (count > 0) {
      if (cost > player.money) return Left(PlayerNotEnoughMoney)
      if (getItemCount(shop.inventory)(itemType) < count) Left(ShopNotEnoughInventory) else {
        val (shopInv, playerInv) = moveItem(shop.inventory, player.inventory, itemType, count)
        Right(player.copy(money = player.money - cost, inventory = playerInv), shop.copy(money = shop.money + cost, inventory = shopInv))

      }
    } else if (getItemCount(player.inventory)(itemType) < -count) Left(PlayerNotEnoughInventory) else {
      if (-cost > shop.money) return Left(ShopNotEnoughMoney)

      val (playerInv, shopInv) = moveItem(player.inventory, shop.inventory, itemType, -count)
      Right(player.copy(money = player.money - cost, inventory = playerInv), shop.copy(money = shop.money + cost, inventory = shopInv))

    }


  }

  def trade(model: Model, itemType: ItemType, count: Int): Either[TradeError, Model] = {
    trade_(model.player, model.shop, itemType, count) match {
      case Left(e) => Left(e)
      case Right((player, shop: Shop)) => Right(model.copy(player = player, shop = shop))
    }
  }

  def safeTrade(model: Model, itemType: ItemType, count: Int): Model = trade(model, itemType, count).getOrElse(model)

  sealed trait GameCommand

  case object QuitCommand extends GameCommand

  case class TradeCommand(itemType: ItemType, count: Int) extends GameCommand

  def execute_(gameCommand: GameCommand, model: Model): Either[String, Model] = {
    gameCommand match {
      case QuitCommand => Right(model)
      case TradeCommand(itemType, count) => trade(model, itemType, count) match {
        case Left(te) => Left(te.toString())
        case Right(m) => Right(progressTime(m)(1))
      }
    }
  }

  def safeExecute(gameCommand: GameCommand, model: Model): Model = execute_(gameCommand, model).getOrElse(model)

}
