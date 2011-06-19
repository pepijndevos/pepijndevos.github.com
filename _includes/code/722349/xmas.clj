(ns xmas)

(doseq [line (concat (take 9 (map #(apply str
                                          (take (+ (rem % 3)
                                                   (int (/ % 3)))
                                                (repeat "*")))
                                  (range 20)))
                     (take 2 (repeat "*")))]
  (println (format "%1$30s*%1$s" line)))

;                              *
;                             ***
;                            *****
;                             ***
;                            *****
;                           *******
;                            *****
;                           *******
;                          *********
;                             ***
;                             ***