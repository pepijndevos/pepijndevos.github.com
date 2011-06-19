(ns game)

(def world {:foo #<fooinstance> :bar #<barinstance>})

(def window (JPanel. 800 800))

(doseq [frame (iterate #(map act %) world)
        obj frame]
  (draw obj window))
