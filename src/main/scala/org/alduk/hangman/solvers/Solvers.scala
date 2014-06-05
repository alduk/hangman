package org.alduk.hangman.solvers

import org.alduk.hangman._
import scala.annotation.tailrec
import org.alduk.hangman.dictionary.Dictionary

/**
 * Solvers used in game for resolving tasks.
 * Can be extended to support gui,web
 *
 * @author a.kudla
 *
 */
trait Solvers {

  /**
   * Injected solver.
   * @return
   */
  val solver: Solver

  /**
   * Solver interface for solving game.
   * @author a.kudla
   *
   */
  trait Solver {
    def solve(state: State): State
  }

  class HumanSolver extends Solver {

    @tailrec
    final def solve(state: State): State = {
      state match {
        case cont @ Cont(word, attempts, guesses) => {
          println(state)
          val s = Hangman.guess(cont, readChar())
          solve(s)
        }
        case a => a
      }
    }
  }

  /**
   * Simple Alphabet Solver.
   * Solves game by simply itarating letters in alphabet.
   * Alphabet can be ordered by letters frequency, to increase chance to win.
   * @author a.kudla
   *
   */
  class AlphabetSolver(alphabet: String) extends Solver {
    @tailrec
    final def solve2(index: Int, state: State): State = {
      state match {
        case cont @ Cont(word, attempts, guesses) => {
          println(state)
          if (alphabet.length < index) {
            val ch = alphabet(index)
            val s = Hangman.guess(cont, ch)
            solve2(index + 1, s)
          } else {
            Defeat(cont)
          }
        }
        case a => a
      }
    }

    final def solve(state: State): State = {
      solve2(0, state)
    }
  }

  /**
   * Simple Dictionary Solver.
   * Solves game by
   * 1.Filtering dictionary with regular expressions
   * 2.Rebuilding letters frequencies with filtered dictionary and guesses made.
   * 3.Guessing letter with highest frequency
   * (if filtered dictionary empty alphabet frequency algorithm used)
   *
   * @author a.kudla
   *
   */
  class DictionarySolver(dictionary: Dictionary) extends Solver {
    @tailrec
    final def solve2(dictionary: Dictionary, state: State): State = {
      state match {
        case cont @ Cont(word, attempts, guesses) => {
          println(state)
          val (d, ch) = dictionary.guess(cont)
          val s = Hangman.guess(cont, ch)
          solve2(d, s)
        }
        case a => a
      }
    }

    def solve(state: State): State = solve2(dictionary, state)
  }

}