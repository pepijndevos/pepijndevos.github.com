(ns cooperative)

(defn cooperative-tasks
  "Trampoline over a queue for
  cooperative multitasking"
  [fs]
  (if (not-any? fn? fs)
    fs
    (recur
      (doall (for [f fs]
        (if (fn? f)
          (f)
          f))))))


(comment
  (declare my-odd?)

  (defn my-even? [n]
    (if (zero? n)
      true
      #(my-odd? (dec (Math/abs n)))))

  (defn my-odd? [n]
    (if (zero? n)
      false
      #(my-even? (dec (Math/abs n)))))

  (cooperative-tasks [#(my-odd? 50009) #(my-odd? 50008) #(my-even? 50008) #(my-even? 50009)])
)
