package dev.jans


object Main extends App {
  println("Hello Scala Super Spacefarer")
  val game = Game.Model()
  println(game)
  val game2 = Game.trade(game, Items.Medicines, 11)
  println(game2)
  val game3 = Game.trade(game2, Items.Metals, -2)
  println(game3)

}
