(defprotocol Spinnable
  (spin [this] "Return a seq walking the opposite direction as this"))

(defn iter-bi [x f b]
  (reify
    Spinnable
    (spin [_] (iter-bi x b f))
    clojure.lang.ISeq
    (first [_] x)
    (more [_] (iter-bi (f x) f b))
    (next [this] (rest this))   ; same as rest since all iter-bi's are infinite seqs
    (seq [this] this)
    (equiv [_ _] false)))       ; cheater!

(extend-type clojure.lang.LazySeq
  Spinnable
  (spin [this] (spin (seq this))))

(->> (iter-bi 0 inc dec)
     (drop 4)
     spin
     (take 10))
;=> (5 4 3 2 1 0 -1 -2 -3 -4)
