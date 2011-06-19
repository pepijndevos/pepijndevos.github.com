(ns test
  (:import [java.util.concurrent
            BlockingQueue
            LinkedBlockingQueue
            SynchronousQueue
            PriorityBlockingQueue
            CyclicBarrier])
  (:use clojure.test)
  (:refer-clojure :exclude [seque]))

;; some BlockingQueues are...
;; not ordered
;; not finite
;; unable to contain nil
;; without content
;; mutable
;; dropping or recieving extra items

;; seque...
;; fills the que on another thread
;; propagates errors
;; replaces nil with a sentinel
;; detects the end of the input seq
;; can be consumed on any thread

(defn seque
  "Creates a queued seq on another (presumably lazy) seq s. The queued
  seq will produce a concrete seq in the background, and can get up to
  n items ahead of the consumer. n-or-q can be an integer n buffer
  size, or an instance of java.util.concurrent BlockingQueue. Note
  that reading from a seque can block if the reader gets ahead of the
  producer."
  {:added "1.0"}
  ([s] (seque 100 s))
  ([n-or-q s]
   (let [^BlockingQueue q (if (instance? BlockingQueue n-or-q)
                             n-or-q
                             (LinkedBlockingQueue. (int n-or-q)))
         NIL (Object.) ;nil sentinel since BQ doesn't support nils
         s (map (fnil identity NIL) s)
         channel (LinkedBlockingQueue.)
         fill (fn fill [s]
                (try
                  (if (seq s)
                    (do
                      (.put channel #(.take q))
                      (.put q (first s))
                      (recur (next s)))
                    (.put channel #(throw (InterruptedException.))))
                  (catch Exception e
                    (.put channel #(throw e)))))
         fut (future (fill s))
         drain (fn drain []
                 (lazy-seq
                   (try
                     (cons ((.take channel)) (drain))
                     (catch InterruptedException e nil))))]
     (map #(if (identical? % NIL) nil %) (drain)))))

(defn trickle
  [slow]
  (map #(do (Thread/sleep 100) %) slow))

(deftest identity?
  (is (= (seque (range 10)) (range 10))))

(deftest priority
  (is (= 4 (count (seque (PriorityBlockingQueue.)
             [:a :b :c :a])))))

(deftest synchronous
  (is (= (seque (SynchronousQueue.)
                (range 10))
         (range 10))))

(deftest nils
  (is (= (seque [nil true false []])
         [nil true false []])))

(deftest errors
  (is (thrown? Throwable
               (dorun (seque (map #(throw (Exception. (str %))) (range 10)))))))

(deftest threads
  (let [s (seque (SynchronousQueue.) (range 10))
        f1 (future (doall s))
        f2 (future (doall s))]
    (is (= (range 10) @f1 @f2))))

(defn moronic [] ; untestable
  (let [q (LinkedBlockingQueue. 100)]
    (future (dotimes [_ 1000] (Thread/yield) (.poll q)))
    (future (dotimes [_ 1000] (Thread/yield) (.offer q :foo)))
    (seque q (range 1000))))
