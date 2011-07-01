(ns ponger.core
  (:require clojure.string))

(defn inflate-mxl [file]
  (let [z (java.util.zip.ZipFile. file)]
    (slurp
      (.getInputStream z
        (.nextElement (.entries z))))))

(defn parse-musicxml [file]
  (let [parser   (org.jfugue.parsers.MusicXmlParser.)
        renderer (org.jfugue.MusicStringRenderer.)]
    (.addParserListener parser renderer)
    (.parse parser (inflate-mxl file))
    (.getPattern renderer)))

(defn notes [tokens]
  (filter #(re-matches #"[A-GR].*" %) tokens))

(defn voices [tokens]
  (let [[f [_ & r]] (split-with (complement #(.startsWith % "V")) tokens)]
    (when (seq f)
      (cons f (voices r)))))

(defn make-chain [tokens]
  (into {}
    (for [[k slice] (group-by first (partition 2 1 tokens))]
      [k (map last slice)])))

(defn walk-chain [chain start]
  (let [nxt (rand-nth
              (get chain start
                   (apply concat (vals chain))))]
    (cons
      nxt
      (lazy-seq
        (walk-chain chain nxt)))))

(defn generate [path]
  (-> path
    parse-musicxml
    (.getTokens)
    notes
    make-chain
    (walk-chain nil)))

(defn play [token-seq]
  (let [player (org.jfugue.StreamingPlayer.)]
    (doseq [token token-seq]
      (.stream player token))))

(defn save [token-seq path]
  (.saveMidi (org.jfugue.Player.)
         (clojure.string/join " " token-seq)
         (java.io.File. path)))

