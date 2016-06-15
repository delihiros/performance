(ns performance.core
  (:require [profile.core :refer :all]))

(def sample-hashmap
  (zipmap (take 10000 (range)) (take 10000 (range))))

(defn into-mapper
  [f m]
  (-> m
      (map #([(first %) (f (second %))]) m)
      (into {})))

(defn reduce-mapper
  [f m]
  (reduce-kv #(assoc %1 %2 (f %3)) {} m))

(defn reduce-mapper-transient
  [f m]
  (persistent! (reduce-kv #(assoc! %1 %2 (f %3)) (transient {}) m)))

(defn for-mapper
  [f m]
  (into {}
        (for [[key value] m]
          {key (f value)} )))

(profile-vars into-mapper reduce-mapper reduce-mapper-transient for-mapper)

(defn -main [& args]
  (profile {}
           (dotimes [n 100]
             (into-mapper inc sample-hashmap)
             (reduce-mapper inc sample-hashmap)
             (reduce-mapper-transient inc sample-hashmap)
             (for-mapper inc sample-hashmap))))
