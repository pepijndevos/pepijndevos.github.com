(reduce #(map + %1 %2) (partition 5 (range 1e6)))
java.lang.StackOverflowError

(reduce #(pmap + %1 %2) (partition 5 (range 1e6)))
(99999500000 99999700000 99999900000 100000100000 100000300000)