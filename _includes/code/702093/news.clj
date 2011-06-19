(ns news
  (:refer-clojure :exclude [resolve])
  (:use clojure.contrib.json)
  (:import [java.io BufferedReader InputStreamReader]
           [java.net URL]))

(def cred "") ; username:password

(def json (let [con (.openConnection (URL. "http://stream.twitter.com/1/statuses/sample.json"))]
            (.setRequestProperty con "Authorization" (str "Basic "
                                                          (.encode (sun.misc.BASE64Encoder.) (.getBytes cred))))
            (BufferedReader. (InputStreamReader. (.getInputStream con)))))

(def urls (agent (list)))

(def resolve (memoize (fn [url]
  (try
    (let [con (doto (.openConnection (URL. url))
                (.setInstanceFollowRedirects false)
                (.connect))
          loc (.getHeaderField con "Location")]
      (.close (.getInputStream con))
      (if loc loc url))
    (catch Exception _ url)))))

(defn top [urls]
  (reduce #(if (> (val %1) (val %2))
             %1 %2)
          (frequencies @urls)))

(future (doseq [tweet (repeatedly #(read-json json))
                url (:urls (:entities tweet))]
          (send-off urls #(conj % (resolve (:url url))))))
