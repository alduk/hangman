package org.alduk.hangman.readers

import org.alduk.hangman._
import scala.io.Source
import java.util.Random

/**
 * Readers used in game for loading creating tasks.
 * Can be extended to support db,web...
 *
 * @author a.kudla
 *
 */
trait TaskReaders {

  /**
   * Injected reader.
   * @return
   */
  val reader: TaskReader

  /**
   * Reader interface for creating new tasks
   * @author a.kudla
   *
   */
  trait TaskReader {
    def newTask: State
  }

  /**
   * Simple SCV Reader.
   * Reads tasks from file system.
   * Uses '|' as delimeter.
   * @author a.kudla
   *
   */
  class CSVReader(fileName: String) extends TaskReader {
    val rand = new Random(System.currentTimeMillis());
    lazy val problems = {
      val lines = Source.fromFile(fileName).getLines.map(_.split("\\|"))
      (lines map { case Array(word, attempts) => Cont(word, attempts.toInt, List()) }).toSeq
    }
    def newTask = problems(rand.nextInt(problems.length))
  }
}