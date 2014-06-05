package org.alduk.hangman

import scala.util._
import readers._
import runners._
import solvers._
import org.alduk.hangman.dictionary.Dictionary

/**
 * Hangman game main class
 * @author a.kudla
 *
 */
object Main extends App {

  /**
   * Base game trait witch declare game modules dependencies.
   * @author a.kudla
   *
   */
  trait Game extends TaskReaders with Runners with Solvers

  //Human console game 
  def humanSolver = new Game {
    val reader = new CSVReader(args(0))
    val solver = new HumanSolver
    val runner = new ConsoleRunner
  }

  //AI game with simple alphabet solver
  def alphabeticSolver = new Game {
    val reader = new CSVReader(args(0))
    val solver = new AlphabetSolver("abcdefghijklmnopqrstuvwxyz'")
    val runner = new ConsoleRunner
  }

  //AI game with simple frequency alphabet solver
  def frequencySolver = new Game {
    val reader = new CSVReader(args(0))
    val solver = new AlphabetSolver("etaoinsrhldcumfpgwyb'vkxjqz")
    val runner = new ConsoleRunner
  }

  //AI game with dictionary solver
  def dictionarySolver = new Game {
    val reader = new CSVReader(args(0))
    val solver = new DictionarySolver(Dictionary("dict.txt"))
    val runner = new ConsoleRunner
  }

  Try {
    dictionarySolver.runner.run
  } recover {
    case ex =>
      println(s"Unexpected problem found: ${ex.getLocalizedMessage()}")
      ex.printStackTrace()
  }
}