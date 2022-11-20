package dev.jans


object Main extends App {
  println("Hello Scala Super Spacefarer")
  val game = Game.Model()
  println(game)
  val game2 = Game.progressTime(Game.trade(game)(Items.Metals, 3, Items.shopPrices))(1)
  println(game2)
  val game3 = Game.trade(game2)(Items.Metals, -1, Items.shopPrices)
  println(game3)
}
