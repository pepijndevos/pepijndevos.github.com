(ns slouchdb)

; Fast scalable in-memory concurrent NoSQL database
(def db (ref {:bob  {:age 59 :sex :male}
              :bill {:age 17 :sex :male}
              :mary {:age 28 :sex :female}}))

;; Views ;;

(defn total-age []
  (reduce + (pmap (comp :age val) @db))) ; Parallel MapReduce!

;; Examples ;;

(println (apply str (interpose \newline
  ["Get an item"
  (:bob @db)
  "MapReduce"
  (total-age)
  "Add/update an item"
  (dosync (alter db assoc-in [:bill :age] 25))
  "Delete an item"
  (dosync (alter db dissoc :mary))])))

; For scaling, press CMD or CTRL and + or -

; For a persistent version, see java.io.Serializable

; For a REST API, check out Moustache and Aleph.
