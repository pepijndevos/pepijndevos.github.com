(ns seqex
  (:import [clojure.lang
            Fn
            IPersistentVector
            IPersistentMap]))

(defn cps [funct cont]
  (fn [[f & r]]
    (when-let [f (funct f)]
      (when-let [r (cont r)]
        (if (true? r)
          (if (true? f) [] [f])
          (if (true? f) r (cons f r)))))))
;    (and (funct f) (cont r))))

(def _ (constantly true))

(def end nil?)

(defmulti matcher type)

(defn match [pattern items]
  ((reduce #(cps (matcher %2) %1)
           (rseq pattern))
     items))

(defmethod matcher :default [l]
  (partial = l))

(defmethod matcher Fn [f] f)

(defmethod matcher IPersistentVector [pattern]
  (partial match pattern))

(defmethod matcher IPersistentMap [pattern]
  (fn [m]
    (every? identity
      (for [[k v] pattern]
        (when-let [mv (get m k)]
          ((matcher v) mv))))))

(defmethod matcher java.util.regex.Pattern [pattern]
  (partial re-find pattern)) ; returns match

(defn store [pattern]
  (fn [s]
    (when ((matcher pattern) s)
      s)))

(defmacro cond-let
  [& clauses]
    (when clauses
      (list `if-let (first clauses)
            (if (next clauses)
                (second clauses)
                (throw (IllegalArgumentException.
                         "cond-let requires an even number of forms")))
            (cons `cond-let (next (next clauses))))))
