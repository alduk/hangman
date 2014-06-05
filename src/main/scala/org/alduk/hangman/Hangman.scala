package org.alduk.hangman

/**
 * Represents current state of the game
 * @author a.kudla
 *
 */
sealed trait State

/**
 * Represents current game info.
 * Further guessing is possible from this state.
 *
 */
case class Cont(word: String, attempts: Int, guesses: List[Char]) extends State {
  //Word delimeters in game.
  val puctuations = """,."-:;?!() """
  val maskedWord = mask(word)
  val maskedWords = words(word) map mask
  def mask(w: String) = w.map(a => if (guesses.contains(a.toLower) || puctuations.contains(a.toLower)) a else '_')
  def words(w: String) = w.split(s"[$puctuations]").map(_.trim).filter(_.length > 0).toSeq
  override def toString = s"${maskedWord},   ${attempts},    ${guesses}"
}

/**
 * Terminal state. User win.
 *
 */
case class Win(state: Cont) extends State {
  override def toString = s"Win:${state}"
}

/**
 * Terminal state. User loose.
 *
 */
case class Defeat(state: Cont) extends State {
  override def toString = s"Defeat:${state}"
}

/**
 * Hangman object.
 * Contains helper methods to determing current game state and guessing results.
 * Uses unmasked data, to prevent mutability.
 */
object Hangman {

  /**
   * Checks whether state is solved
   * @param state
   * @return
   */
  def win(state: Cont) = !state.maskedWord.contains('_')

  /**
   * Changes state if needed
   * @param state
   * @return
   */
  def checkWin(state: Cont): State = {
    if (win(state))
      Win(state)
    else if (state.attempts == 0)
      Defeat(state)
    else
      state
  }

  /**
   * Apply guess to state.
   * @param state
   * @param guess
   * @return new state or old one if guess repeated.
   */
  def guess(state: Cont, guess: Char): State = {
    val res = if (state.guesses.contains(guess)) {
      state
    } else if (state.word.contains(guess)) {
      state.copy(guesses = guess :: state.guesses)
    } else {
      state.copy(attempts = state.attempts - 1, guesses = guess :: state.guesses)
    }
    checkWin(res)
  }

}