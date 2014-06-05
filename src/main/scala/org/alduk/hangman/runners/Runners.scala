package org.alduk.hangman.runners

import org.alduk.hangman._
import org.alduk.hangman.solvers._
import org.alduk.hangman.readers._
import scala.annotation.tailrec

/**
 * Runners used in game for providing game loop.
 * Can be extended to support gui,web
 *
 * @author a.kudla
 *
 */
trait Runners { this: Solvers with TaskReaders =>

  /**
   * Injected runner.
   * @return
   */
  val runner: Runner

  /**
   * Runner interface for running game.
   * @author a.kudla
   *
   */
  trait Runner {
    def run: State
  }

  /**
   * Simple Console Runner.
   * @author a.kudla
   *
   */
  class ConsoleRunner extends Runner {

    def run(): State = {
      val result = solver.solve(reader.newTask)
      println(result)
      println("Play again ?")
      if ("Yes".equalsIgnoreCase(readLine()))
        run()
      result
    }
  }

}