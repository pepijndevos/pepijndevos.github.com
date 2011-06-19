(ns biseq)

(defprotocol Spinnable (spin [_]))

(defn biseq [start fwd bwd]
  (reify
    clojure.lang.ISeq
    (first [this] start)
    (next [this] (biseq (fwd start) fwd bwd))
    (more [this] (.next this))
    (seq [this] this)
    Spinnable
    (spin [this] (biseq start bwd fwd))))

(extend-type clojure.lang.LazySeq
  Spinnable
  (spin [this] (spin (seq this))))

(defn spin [seq]
  (.spin seq))

(let [s (biseq 0 inc dec)]
  (println (take 10 s))
  (println (take 10 (spin s)))
  (println (take 20 ((fn f [s]
                       (lazy-seq (cons
                                   (first s)
                                   (f (rest (if (zero? (rem (first s) 5))
                                              (spin s)
                                              s)))))) s))))
;(0  1  2  3  4  5  6  7  8  9)
;(0 -1 -2 -3 -4 -5 -6 -7 -8 -9)
;(0 -1 -2 -3 -4 -5 -4 -3 -2 -1 0 -1 -2 -3 -4 -5 -4 -3 -2 -1)