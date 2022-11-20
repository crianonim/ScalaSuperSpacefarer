package dev.jans

import org.scalatest.funsuite.AnyFunSuite

class GameTest extends AnyFunSuite {
  val poorPlayer = Game.Player(10, Game.startingInv)
  val richPlayer = Game.Player(1000, Game.startingInv)
  val poorShop = Items.Shop(Map(Items.Metals -> 20, Items.Medicines -> 10, Items.Food -> 5), Items.exampleShopPrices, 20)
  test("Game.trade can't trade when count = 0 ") {
    assert(Game.trade_(richPlayer, poorShop, Items.Food, 0) == Left(Game.ZeroTradeCount))
  }

  test("Game.trade can't buy when player hasn't enough money") {
    assert(Game.trade_(poorPlayer, poorShop, Items.Metals, 10) == Left(Game.PlayerNotEnoughMoney))

  }
  test("Game.trade can't sell when shop hasn't enough money") {
    assert(Game.trade_(poorPlayer, poorShop, Items.Metals, -10) == Left(Game.ShopNotEnoughMoney))

  }
  test("Game.trade can't buy when shop hasn't enough stock") {
    assert(Game.trade_(richPlayer, poorShop, Items.Food, 6) == Left(Game.ShopNotEnoughInventory))
  }
  test("Game.trade can't sell when player hasn't enough stock") {
    assert(Game.trade_(richPlayer, poorShop, Items.Food, -1) == Left(Game.PlayerNotEnoughInventory))
  }

  test("Game.trade successful buying") {
    assert(Game.trade_(richPlayer, poorShop, Items.Food, 2) ==
      Right(richPlayer.copy(money = 990, inventory = Map(Items.Metals -> 10, Items.Medicines -> 0, Items.Food -> 2)),
        poorShop.copy(money = 30, inventory = Map(Items.Metals -> 20, Items.Medicines -> 10, Items.Food -> 3))))
  }
  test("Game.trade successful sell") {
    assert(Game.trade_(richPlayer, poorShop, Items.Metals, -2) ==
      Right(richPlayer.copy(money = 1020, inventory = Map(Items.Metals -> 8, Items.Medicines -> 0, Items.Food -> 0)),
        poorShop.copy(money = 0, inventory = Map(Items.Metals -> 22, Items.Medicines -> 10, Items.Food -> 5))))
  }
}

