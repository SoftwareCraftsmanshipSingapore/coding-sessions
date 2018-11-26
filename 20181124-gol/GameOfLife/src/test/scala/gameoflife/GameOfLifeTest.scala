package gameoflife

import org.scalatest.{FlatSpec, FunSuite, Matchers}

class GameOfLifeTest extends FlatSpec with Matchers {

  "An empty world" should "remain empty" in {
    val world = World()
    world.evolve() shouldBe world
  }

  "A world with one living cell at the centre when evolved" should "become empty" in {
    val world = World(Cell(0,0))
    world.isAlive(Cell(0,0)) shouldBe true
    world.evolve().isAlive(Cell(0,0)) shouldBe false
  }


  "cells in a square formation" should "remain alive after the world is evolved" in {
    val world = World(Cell(0, 0), Cell(1, 0), Cell(0, 1), Cell(1, 1))
    world.getNumberOfAliveNeighbours(Cell(0,0)) shouldBe 3
    world.evolve() shouldBe world
  }

  "cells in another square formation" should "remain alive after the world is evolved" in {
    val cells = Seq(Cell(0, 0), Cell(-1, 0), Cell(0, -1), Cell(-1, -1))
    val world = World(cells: _*)
    cells.foreach(c => world.getNumberOfAliveNeighbours(c) shouldBe 3)
    world.evolve() shouldBe world
  }


  "A dead cell with three adjacent live cells" should "come alive" in {
    val world = World(Cell(0,0), Cell(1,0), Cell(0,1))
    val new_world = world.evolve()
    new_world.isAlive(Cell(1,1)) shouldBe true
    new_world.isAlive(Cell(0,0)) shouldBe false
    new_world.isAlive(Cell(1,0)) shouldBe false
    new_world.isAlive(Cell(0,1)) shouldBe false
  }

}
