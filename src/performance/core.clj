(ns performance.core
  (:require [profile.core :refer :all]))

(def large-hashmap
  (zipmap (take 100000 (range)) (take 100000 (range))))

(def small-hashmap 
  (zipmap (take 100 (range)) (take 100 (range))))

(defn into-mapper
  [f m]
  (-> m
      (map #([(first %) (f (second %))]) m)
      (into (empty m))))

(defn into-mapper-v2
  [f m]
  (into (empty m) (for [[k v] m] [k (f v)])))

(defn into-mapper-v3
  [f m]
  (into (empty m) (map (fn [e] [(key e) (f (val e))]) m)))

(defn reduce-mapper
  [f m]
  (reduce-kv #(assoc %1 %2 (f %3)) (empty m) m))

(defn reduce-mapper-transient
  [f m]
  (persistent! (reduce-kv #(assoc! %1 %2 (f %3)) (transient (empty m)) m)))

(defn for-mapper
  [f m]
  (into (empty m)
        (for [[key value] m]
          {key (f value)} )))

(defn map-vals-iterable
  [f m]
  (let [k-it (.keyIterator m)
        v-it (.valIterator m)]
    (loop [ret (empty m)]
      (if (.hasNext k-it)
        (let [k (.next k-it)
              v (.next v-it)]
          (recur (assoc ret k (f v))))))))
          
(defn map-vals-persistent
  [f m]
  (persistent!
    (reduce-kv (fn [m k v] (assoc! m k (f v)))
               (transient (empty m)) m)))

(defn map-vals-switch
  [f m])

(profile-vars 
  into-mapper
  into-mapper-v2
  into-mapper-v3
  reduce-mapper
  reduce-mapper-transient
  for-mapper
  map-vals-iterable
              map-vals-persistent)

(defn -main [& args]
  (profile {}
           (dotimes [n 100]
             (into-mapper inc small-hashmap)
             (into-mapper-v2 inc small-hashmap)
             (into-mapper-v3 inc small-hashmap)
             (reduce-mapper inc small-hashmap)
             (reduce-mapper-transient inc small-hashmap)
             (for-mapper inc small-hashmap)
             (map-vals-iterable inc small-hashmap)
             (map-vals-persistent inc small-hashmap)))
  (profile {}
           (dotimes [n 100]
             (into-mapper inc large-hashmap)
             (into-mapper-v2 inc large-hashmap)
             (into-mapper-v3 inc large-hashmap)
             (reduce-mapper inc large-hashmap)
             (reduce-mapper-transient inc large-hashmap)
             (for-mapper inc large-hashmap)
             (map-vals-persistent inc large-hashmap))))
