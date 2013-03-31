(ns test
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic
        clojure.pprint)
  (:require [clojure.core.logic.fd :as fd]))

(defn grid [n] (repeatedly (* n n) lvar))

(def rows partition)

(defn cols [n grid] (apply map list (rows n grid)))

(defn sum [ls res]
  (conde
    ((== ls []) (== res 0))
    ((== ls [res]))
    ((fresh [h t inter]
       (conso h t ls)
       (fd/+ h inter res)
       (sum t inter)))))

(defn magic [n]
  (let [g (grid n)
        nums (range 1 (inc (* n n)))
        ndom (apply fd/domain nums)
        lsum (/ (apply + nums) n)
        lines (concat (rows n g) (cols n g))]
    (->> (run 1 [q]
           (everyg #(fd/dom % ndom) g)
           (fd/distinct g)
           (everyg #(sum % lsum) lines)
           (== q g))
      first
      (rows n)
      (map println)
      dorun)))
