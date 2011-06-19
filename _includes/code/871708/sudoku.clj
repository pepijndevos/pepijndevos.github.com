(ns sudoku
  (:use clojure.pprint)
  (:require [clojure [set :as s]]))

(def _ nil)

(def board [_ 7 _ _ _ 8 _ _ 5
            _ _ 8 3 _ 1 _ _ _
            1 6 3 _ _ 4 8 9 7
            9 8 _ _ _ _ 4 _ _
            2 _ _ 1 _ 5 _ _ 9
            _ _ 4 _ _ _ _ 3 1
            3 4 6 9 _ _ 1 7 8
            _ _ _ 4 _ 7 9 _ _
            7 _ _ 8 _ _ _ 6 _])

(def hard [_ 6 _ _ 5 _ 3 2 _ ; 3 added
           _ _ _ 3 _ _ _ 9 _
           7 _ _ 6 _ _ _ 1 _
           _ _ 6 _ 3 _ 4 _ _
           _ _ 4 _ 7 _ 1 _ _
           _ _ 5 _ 9 _ 8 _ _
           _ 4 _ _ _ 1 _ _ 6
           _ 3 _ _ _ 8 _ _ _
           _ 2 9 _ 4 _ _ 5 _]) ; can be either 7 or 9

(def from-blocks [0 9 18 1 10 19 2 11 20 3 12 21 4 13 22 5 14 23 6 15 24 7 16 25 8 17 26 27 36 45 28 37 46 29 38 47 30 39 48 31 40 49 32 41 50 33 42 51 34 43 52 35 44 53 54 63 72 55 64 73 56 65 74 57 66 75 58 67 76 59 68 77 60 69 78 61 70 79 62 71 80])
(def to-blocks [0 1 2 27 28 29 54 55 56 3 4 5 30 31 32 57 58 59 6 7 8 33 34 35 60 61 62 9 10 11 36 37 38 63 64 65 12 13 14 39 40 41 66 67 68 15 16 17 42 43 44 69 70 71 18 19 20 45 46 47 72 73 74 21 22 23 48 49 50 75 76 77 24 25 26 51 52 53 78 79 80])

(defn reduceable-board [board]
  (for [cell board]
    (if (number? cell)
      cell
      (set (range 1 10)))))

(defn prune [group]
  (let [nrs (set (filter number? group))]
    (for [cell group]
      (cond
        (number? cell) cell
        (= (count cell) 1) (first cell)
        :else (s/difference cell nrs)))))

(defn transpose [old mapping]
  (apply assoc (vec old) (interleave mapping old)))

(defn slice [board]
  (let [rows (map prune (partition 9 board))
        cols (map prune (apply map list rows))
        blocks (map prune (partition 9 (transpose (flatten cols) to-blocks)))]
    (transpose (flatten blocks) from-blocks)))

(defn sudoku [board]
  (loop [board (reduceable-board board)]
    (let [next-board (slice board)]
      (pprint (partition 9 next-board))
      (if (or (= board next-board)
              (every? number? next-board)
              (some #(= #{} %) next-board))
        next-board
        (recur next-board)))))