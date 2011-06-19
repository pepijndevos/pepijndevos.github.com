(ns pigeonhole-sort)

(defn int-sort [s]
  (let [listmap (reduce #(update-in
                           (update-in %1 [%2] (fnil inc 0))
                           [:max] max %2) {:max 0} s)]
    (mapcat #(repeat (get listmap % 0) %)
            (range (inc (:max listmap))))))
