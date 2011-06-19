(ns markov-stone)

;{:won {:same 0, :inferior 0, :superior 0}, :lost {:same 0, :inferior 0, :superior 0}, :draw {:same 0, :inferior 0, :superior 0}}

(defn roulette [slices]
  (let [slices (sort-by val slices)
        total (reduce + (vals slices))
        r (rand total)
        cumulative (map vector (keys slices) (reductions + (vals slices)))]
    (first (first (drop-while #(< (last %) r) cumulative)))))

(defn guess [markov prev state]
  (let [inferior {:s :p, :p :r, :r :s}
        superior (clojure.set/map-invert inferior)
        pred (->> markov
                  state
                  roulette)]
    (case pred
          :same (superior prev)
          :superior (inferior prev)
          :inferior prev)))

(defn check [user cpu]
  (if (= user cpu)
    :draw
    (if (= user (cpu {:s :p, :p :r, :r :s}))
      :lost
      :won)))

(defn relation [prev cur]
  (if (= prev cur)
    :same
    (if (= prev (cur {:s :p, :p :r, :r :s}))
      :superior
      :inferior)))

(defn play [markov prev state score]
  (println "Choose your weapon[r/p/s]!")
  (if-let [user (#{:r :p :s} (keyword (read-line)))]
    (let [names {:r "ROCK" :p "PAPER" :s "SCISSORS"}
          cpu (guess markov prev state)
          result (check user cpu)
          score (case result
                      :won (inc score)
                      :lost (dec score)
                      :draw score)]
      (println (names cpu))
      (println "Game is" (name result) "- Score:" score)
      (recur (update-in markov [result (relation prev user)] inc) user result score))
    (do
      (println "Invalid input; Final score:" score)
      markov)))

(println (play {:won {:same 20, :inferior 17, :superior 16}, :lost {:same 16, :inferior 13, :superior 38}, :draw {:same 23, :inferior 18, :superior 21}} :r :draw 0))
