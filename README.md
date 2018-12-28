# tictactoe-cli

TicTacToe game written in Clojure, running on the command line with a human player verses the computer program.

This project is from the[TicTacToe game from Practicalli Clojure](https://practicalli.github.io/clojure/games/tictactoe-cli/)

## Usage

The current game solution has not been written using TDD, so is put on a branch called `solution-no-tests`.

To use the current version of the game, checkout this branch first

```bash
git checkout solution-no-tests
```

### Playing the game via the command line

The game can be played in the command line by starting a REPL from the root of the project (where project.clj file is)

```bash
lein repl
```
Once the repl prompt is displayed, start the game by including the game namespace

```clojure
(require 'tictactoe-cli.core)
```

The current game board will be displayed, followed by a prompt for the next move.  Each time a move is made, the new game board will be displayed, along with a prompt for the next move.

### Playing the game in Spacemacs/Emacs

You can play the game in Spacemacs / Emacs too, using the mini-buffer as the command line prompt (Stdin).

Start the REPL using `cider-jack-in` (`, '`) and open the REPL buffer (`, s s`).

Change the REPL to the `tictactoe-cli.core` namespace, (`, s n`).

Evaluate the buffer to start the game, (`, e b`).

Enter the next move value in the mini-buffer (not the REPL window) when you see the `Stdin` prompt.


## License

Copyright © 2018 Practicalli

Distributed under the Creative Commons Attribution Share-Alike 4.0 International.
