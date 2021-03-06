hangman
=======
Sample implementation of hangman game.

This implementation based on Cake pattern.
http://jonasboner.com/2008/10/06/real-world-scala-dependency-injection-di

Main compositions units:
------------------------

TaskReaders - used to load tasks for game. Additional implementations and task sources can be done by extending TaskReader trait.<br/>
Runners - implements main game loop. Can be extended to run game using GUI,web etc.<br/>
Solvers - implements sample solvers  for the game. Can be extended to use more complex AI, GUI, Web.<br/>
Hangman - game object. Implements game states, transitions and helper methods.

Typical usage:

```scala
val game  = new Game {
    val reader = new CSVReader(args(0))
    val solver = new DictionarySolver(Dictionary("dict.txt"))
    val runner = new ConsoleRunner
}

game.runner.run
```
