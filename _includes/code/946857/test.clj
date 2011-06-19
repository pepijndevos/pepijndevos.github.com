(match
 [1
  [3 _ end]
  {:a even?, :b #(= 2 (count %))}
  end]
 [1 [3 5] {:a 4, :b [1 2], :d :a}])
; [] (is truthy)

(match [1 2 3 _] [1 2 3 4 5 6 7 8])
; []

(let [[x y] (match
              [1 #"\w.+?\w" 3 (store _) end]
              [1 "foo bar"  3        4])]
  (dotimes [_ y] (println x)))
; foo
; foo
; foo
; foo
; nil
