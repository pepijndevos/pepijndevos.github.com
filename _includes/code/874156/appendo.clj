(defn-e append-o [x y z]
  ([() _ y])
  ([[?a . ?d] _ [?a . ?r]] (append-o ?d y ?r)))

(defn append-o [l s out]
  (cond-e
   ((null-o l) (== s out))
   ((exist [a d res]
      (cons-o a d l)
      (cons-o a res out)
      (append-o d s res)))))