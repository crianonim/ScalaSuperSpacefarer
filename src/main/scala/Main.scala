package dev.jans

import Game.{GameCommand, Model, TradeCommand, execute_, safeExecute}


object Main extends App {
  println("Hello Scala Super Spacefarer")
  val game = Game.Model()

  val end = for {
    game2 <- Game.trade(game, Items.Medicines, 11)
    game3 <- Game.trade(game2, Items.Metals, -2)
  } yield (game3)
  println(end)

  val commands: List[GameCommand] = List(TradeCommand(Items.Metals, -2), TradeCommand(Items.Medicines, -1), TradeCommand(Items.Medicines, 1))
  val afterExecution = commands
    .foldRight(game)(safeExecute)
  println(afterExecution)

  val executeAndLogErrors: ((Model, List[String]), GameCommand) => (Model, List[String]) =

    ((acc, command) => {
      val result = execute_(command, acc._1)
      result match {
        case Left(err) => (acc._1, acc._2 ++
          List(err))
        case Right(model) => (model, acc._2)
      }
    })

  val executeAndLogAll: ((Model, List[String]), GameCommand) => (Model, List[String]) =

    ((acc, command) => {
      val result = execute_(command, acc._1)
      result match {
        case Left(err) => (acc._1, acc._2 ++
          List(err + command.toString))
        case Right(model) => (model, acc._2 ++ List("Success:" + command.toString))
      }
    })
  val afterExecution_ : (Model, List[String]) = commands
    .foldLeft((game, List(): List[String]))(executeAndLogErrors)
  println(afterExecution_)
  val afterExecution2: (Model, List[String]) = commands
    .foldLeft((game, List(): List[String]))(executeAndLogAll)
  println(afterExecution2)
}
