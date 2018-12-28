;; TicTacToe on the command line
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Author: jr0cket
;; Created Date: 28 December 2018
;;
;; Description:
;; A simple TicTacToe game in Clojure, running on the command line.
;; The game has one human player that takes turns with the computer player.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns tictactoe-cli.core)


;; The game board and its combinations (rules)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; The board is a 3 by 3 grid, so could just be represented by a vector,
;; or if you wanted to represent each row, then a vector of three vectors,
;; with each vector containing 3 elements.

(def starting-board
  "The default board, used when a new game starts"
  [1 2 3 4 5 6 7 8 9])

;;TODO: can we come up with a better default value for the board?



;; As we need to check each row, each column and the two diagonal lines for a winner,
;; lets define each possible line and call these tripples (as a line has 3 elements).

(defn triples
  "All the lines of the game board that should be checked for a winner
   Takes the current board as an argument."
  [board]
  (concat
   (partition-all 3 board)                       ; the rows of the board
   (list
    (take-nth 3 board)                           ; first column
    (take-nth 3 (drop 1 board))                  ; second column
    (take-nth 3 (drop 2 board))                  ; third column
    (take-nth 4 board)                           ; top-left to bottom-right diagonal
    (take-nth 2 (drop-last 2 (drop 2 board)))))) ; top-right to bottom-left diagonal


(defn full-board?
  "Is every cell on the board filled with either a :o or :x ?
   Takes the current board as an argument."
  [board]
  (every? #{:x :o} board))


#_(defn display-board
  "Displays the state of the current board, passed as an argument"
  [board]
  (let [board (map
               #(if (keyword? %)
                  (subs (str %) 1)
                  %)
               board)]
    (println (nth board 0) (nth board 1) (nth board 2))
    (println (nth board 3) (nth board 4) (nth board 5))
    (println (nth board 6) (nth board 7) (nth board 8))))

(defn display-board
  "Displays the state of the current board, passed as an argument"
  [board]
  (let [board (map
               #(if (keyword? %)
                  (name %)
                  %)
               board)]
    (println (nth board 0) (nth board 1) (nth board 2))
    (println (nth board 3) (nth board 4) (nth board 5))
    (println (nth board 6) (nth board 7) (nth board 8))))

;; TODO: can we make the board look prettier, or more board-like,
;; eg. use | - to create a grid.


#_(defn player-name
  "Convert player representation, :o or :x to string, o or x"
  [player]
  (subs (str player) 1))

;; should change this to `name` to make this more abstract.

(name :x)
;; => "x"

(defn player-name
  "Convert player representation, :o or :x to string, o or x"
  [player]
  (name player))







;; Winner detection
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; original code

(defn triple-winner?
  "If a line contains three of the same player, return the player, otherwise return `nil`"
  [triple]
  (if (every? #{:x} triple)
    :x
    (if (every? #{:o} triple)
      :o)))

(triple-winner? [1 2 3])
;; => nil
(triple-winner? [:x 2 3])
;; => nil
(triple-winner? [:x :x 3])
;; => nil
(triple-winner? [:x :x :x])
;; => :x
(triple-winner? [:o 2 3])
;; => nil
(triple-winner? [:o :o 3])
;; => nil
(triple-winner? [:o :o :o])
;; => :o


;; Seems this would be better as a cond statement

#_(defn triple-winner?
  "When there is a complete line of :x or :o characters in any line, then return the winner"
  [triple]
  (cond
    (every? #{:x} triple) :x
    (every? #{:o} triple) :o))


;; Need to declare triples because we refer to it before its defined in the code.
;; Cant we just call this after the triples function??
;; In fact I'll just move tripples to the start as the structure of the board
;; (declare triples)

(defn winner?
  "returns winner if there is one, otherwise nil"
  [board]
  (first
   (filter #{:x :o} (map triple-winner? (triples board)))))

;; (winner? starting-board)                ; failing

;; (map triple-winner? (triples board))

;; Taking turns in the game
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; The game is a series of turns taken alternatively by each player.
;; We can represent the turn as an infinite lazy sequence,
;; allowing us to simply iterate through the turns
;; This means we dont need to manage mutable state regarding who's turn it is.

(def player-sequence
  "Generate an infinite lazy sequence for the player turns"
  (cycle [:o :x]))


;; We need to get a move from the human player, by them entering a move via the keyboard.

(defn next-move
  "Reads the next move from the command line using `read-line` and converts it to an integer value.
   Returns the human move if its value exists in the board, otherwise returns nil."
  [board]
  (let [keyboard-input
        (try
          (. Integer parseInt (read-line))
          (catch Exception e nil))]
    (if (some #{keyboard-input} board)
      keyboard-input
      nil)))


(defn take-turn
  "Ask the players to make a move and inform them when they make an incorrect move."
  [player board]
  (println (str (player-name player) ":") "Select your move (press a number between 1 and 9 then press enter)")
  (loop [move (next-move board)]
    (if move
      (assoc board (dec move) player)

      ;; else
      (do
        (println (str (player-name player) ":") "The move you entered is unavailable, please select a different move")
        (recur (next-move board))))))


(defn play-game
  "The game loop.
  We iterate through the player sequence (alternate player turns)
  until there is a winner or the board is full."
  []
  (loop [board starting-board
         player-sequence player-sequence]
    (let [winner (winner? board)]
      (println "Current board:")
      (display-board board)
      (cond
        winner              (println "Player " (player-name winner) " wins!")
        (full-board? board) (println "The game is a draw.")
        :else
        (recur
         (take-turn (first player-sequence) board)
         (rest player-sequence))))))


(play-game)
