---
layout: post
title: Backwards Game of Life
categories:
- clojure
- core.logic
---

I got a litlte bit nerd sniped by the following video and decided to implement game of life in clojure.core.logic, because any logic program can be evaluated forwards and backwards.

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/g8pjrVbdafY?si=AOnhIGwLQzy3MDaN" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>

Without further ado here is my implementation:

```clojure
(ns pepijndevos.lifeclj
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic)
  (:gen-class))

;; A helper to get the neighbouring cells.
;; Clips to zero.
(defn get-neighbours [rows x y]
  (for [dx (range -1 2)
        dy (range -1 2)
        :when (not (= dx dy 0))]
    (get-in rows [(+ x dx) (+ y dy)] 0)))

;; Produces binary vectors of a certain number of bits.
;; This is used to generate all neighbour combinations.
(defn bitrange [n]
  (sort-by #(apply + %)
           (for [i (range (bit-shift-left 1 n))]
             (vec (map #(bit-and 1 (bit-shift-right i %)) (range n))))))

;; Encode the game of life rules as a 256 element conde.
;; Depending on the number of ones in a vector,
;; the corresponding rule is generated
;; that equates the pattern to the neigbours
;; and the appropriate next state.
;;
;; This can be asked simply what the next state is for
;; given neighbours and current state.
;; OR you could drive it backwards any way you like.
(defn lifegoals [neigh self next]
  (or*
   (for [adj (bitrange 8)
         :let [n (apply + adj)]]
     (cond
       (or (< n 2) (> n 3)) (all (== next 0) (== neigh adj))
       (= n 3)              (all (== next 1) (== neigh adj))
       :else             (all (== next self) (== neigh adj))))))

;; Relate two grids to each other according to the above rules.
;; Applies lifegoals to every cell and its neighbours.
;; in the forwards direction executes one life step,
;; in the backwards direction generates grids
;; that would produce the next step.
(defn stepo [size vars next]
  (let [rows (->> vars (partition size) (map vec) (into []))
        neig (for [x (range size)
                   y (range size)]
               (get-neighbours rows x y))]
    (everyg #(apply lifegoals %) (map vector neig vars next))))

;; Make a grid of unbound variables.
(defn grid [size] (repeatedly (* size size) lvar))

;; Simply execute a life step on the state.
(defn fwdlife [size state]
  (let [vars (grid size)
        next (grid size)]
    (run 1 [q]
         (== q next)
         (== vars state)
         (stepo size vars next))))

;; Produce three backward steps on state.
(defn revlife [size state]
  (let [start (grid size)
        s1 (grid size)
        s2 (grid size)
        end (grid size)]
    (run 1 [q]
          (== q [start s1 s2 end])
          (== end state)
          (stepo size s2 end)
          (stepo size s1 s2)
          (stepo size start s1)
         )))

;; Nicely print the board.
(defn printlife [size grids]
  (doseq [g grids]
    (doseq [row (->> g (partition size) (map vec) (into []))]
      (doseq [t row]
        (print t ""))
      (print "\n"))
    (print "\n")))

;; Test with a glider.
(defn -main [& args]
  (->> [0 0 0 0 0 0
        0 0 0 0 0 0
        0 0 0 1 1 0
        0 0 1 1 0 0
        0 0 0 0 1 0
        0 0 0 0 0 0]
       (revlife 6)
       first
       (printlife 6)))
```

output:

```
$ clj -Mrun
1 0 1 0 1 1 
1 0 0 0 0 1 
0 0 1 0 0 0 
0 0 0 0 0 1 
1 0 1 1 0 0 
1 0 1 1 1 1 

0 1 0 0 1 1 
0 0 0 1 1 1 
0 0 0 0 0 0 
0 1 1 1 0 0 
0 0 1 0 0 1 
0 0 1 0 1 0 

0 0 0 1 0 1 
0 0 0 1 0 1 
0 0 0 0 0 0 
0 1 1 1 0 0 
0 0 0 0 1 0 
0 0 0 1 0 0 

0 0 0 0 0 0 
0 0 0 0 0 0 
0 0 0 1 1 0 
0 0 1 1 0 0 
0 0 0 0 1 0 
0 0 0 0 0 0
```

Sadly, this is nowhere near fast enough to solve the play button problem.