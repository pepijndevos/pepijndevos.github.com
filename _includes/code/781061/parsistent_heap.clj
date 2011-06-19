(ns persistent-heap)

(defn swap [heap idx idy]
  (assoc heap idx (get heap idy) idy (get heap idx)))

(defn children [idx]
  (let [idx (inc (* idx 2))
        idy (inc idx)]
    [idx idy]))

(defn parent [idx]
  (if (not= 0 idx)
    (/ (- idx (if (odd? idx) 1 2)) 2)
    nil))

(defn tree 
  ([heap] (tree heap 0))
  ([heap idx]
   (let [[left right] (children idx)
         node (get heap idx nil)]
     (when node
       [node (tree heap left) (tree heap right)]))))

(defn heap-up 
  ([heap] (heap-up heap >= (dec (count heap))))
  ([heap compfn] (heap-up heap compfn (dec (count heap))))
  ([heap compfn idx]
   (if-let [par (parent idx)]
     (if (compfn (get heap idx) (get heap par))
       (recur (swap heap idx par) compfn par)
       heap)
     heap)))

(defn heap-down
  ([heap] (heap-down (pop (swap heap 0 (dec (count heap)))) >= 0))
  ([heap compfn] (heap-down (pop (swap heap 0 (dec (count heap)))) compfn 0))
  ([heap compfn idx]
   (let [[left right] (children idx)
         leftv (get heap left nil)
         rightv (get heap right nil)
         node (get heap idx nil)]
     (if (and node leftv rightv)
       (cond
         (compfn leftv (max rightv node))
           (recur (swap heap idx left) compfn left)
         (compfn rightv (max leftv node))
           (recur (swap heap idx right) compfn right)
         :else heap)
       heap))))

(deftype PersistentHeap [heap]
         clojure.lang.ISeq
         (first [this] (first heap))
         (next [this] (PersistentHeap. (heap-down heap)))
         (more [this] (.next this))
         (cons [this obj] (PersistentHeap. (heap-up (conj heap obj))))
         (seq [this] (seq heap)))

(defn persistent-heap [coll]
  (into (PersistentHeap. []) coll))

(defn heapsort [coll]
  (->> (persistent-heap coll)
       (iterate rest)
       (take (count coll))
       (map first)))
