package org.alduk.hangman.dictionary

import scala.io.Source
import org.alduk.hangman.Cont
import scala.util.matching.Regex
import org.alduk.hangman.Hangman
import scala.collection.mutable.HashMap

/**
 * Dictionary class which is used in dictionary based AI solver for Hangman.
 * @author a.kudla
 *
 */
class Dictionary(words: List[String], alphabet: String = "etaoinsrhldcumfpgwyb'vkxjqz") {
  import Dictionary._

  /**
   * Filtering dictionary based on current state
   * @param state
   * @return filtered words, based on current state
   */
  def filter(state: Cont): List[String] = {
    val regex = makeRegex(state)
    words.filter(w => regex.foldLeft(false)(_ || w.matches(_)))
  }

  /**
   * Selects next character to guess, based on current game state
   * @param state
   * @return filtered dictionary and character to guess
   */
  def guess(state: Cont): (Dictionary, Char) = {
    val filtered = filter(state)
    val freq = frequencies(state, filtered)
    val index = if (freq.size > 0) freq.toSeq.sortBy(_._2) else remainingLetters(state)
    (new Dictionary(filtered), index(index.length - 1)._1)
  }

  /**
   * Creates frequency sequence from letters which are not present in guesses and based on alphabet
   * Frequency determined only by alphabet
   * @param state
   * @param freq
   * @return frequency sequence
   */
  def remainingLetters(state: Cont): Seq[(Char, Int)] = alphabet
    .filter(ch => !state.guesses.contains(ch))
    .map((_, 1)).reverse

  /**
   *
   * Builds frequency map based on filtered words and current game state
   * @param state
   * @param filtered
   * @return
   */
  def frequencies(state: Cont, filtered: List[String]): Map[Char, Int] = {
    filtered
      .flatMap(_.toList)
      .filter(!state.guesses.contains(_))
      .foldLeft(Map[Char, Int]() withDefaultValue 0) {
        (m, x) => m + (x -> (1 + m(x)))
      }
  }
}

object Dictionary {
  /**
   * Creates new Dictionary from file
   *
   * @param fileName
   * @return
   */
  def apply(fileName: String) = {
    new Dictionary(Source.fromFile(fileName).getLines.map(_.toLowerCase).toList)
  }

  /**
   *
   * Builds regular expression sequence  to filter dictionary based on current game state
   * @param state
   * @return
   */
  def makeRegex(state: Cont): Seq[String] = {
    val unknown = if (state.guesses.length == 0) "." else s"[^${state.guesses.mkString}]"
    state.maskedWords.map(_.replaceAll("_", unknown))

  }
}