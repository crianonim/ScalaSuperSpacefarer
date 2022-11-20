package dev.jans

import org.scalatest.funsuite.AnyFunSuite

class GameTest extends AnyFunSuite {
  test("Game.trade can't buy when not enough money") {
    val model = Game.Model()
    assert(Game.trade(model)(Items.Food, 2222, Items.shopPrices) == model)
  }
}
