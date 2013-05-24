(ns crypto.base16
  (:require [clojure.core.reducers :as r]))

(def charset "0123456789abcdef")
(def charvec (vec (map char charset)))

(defn fold-into-vec [coll]
  "Provided a reducer, concatenate into a vector.
   Note: same as (into [] coll), but parallel."
  (r/fold (r/monoid into vector) conj coll))

;; Encode
(defn byte-to-hex
  [b]
  (let [v (bit-and b 0xFF)]
    [(charvec (bit-shift-right v 4)) (charvec (bit-and v 0x0F))]))

(defn encode
  [^"[B" in]
  (into [] (r/mapcat byte-to-hex in)))

;; Decode
(defn unhexify-two-bytes
  [[a b]]
  (unchecked-byte (+ (bit-shift-left (Character/digit (char a) 16) 4)
                     (Character/digit (char b) 16))))

(defn decode
  [^"[B" in]
  (into [] (r/map (comp char unhexify-two-bytes) (partition 2 2 in))))

(defn decode'
  [^"[B" in]
  (fold-into-vec (r/map (comp char unhexify-two-bytes) (partition 2 2 in))))

(defn decode''
  [^"[B" in]
  (r/foldcat (r/map (comp char unhexify-two-bytes) (partition 2 2 in))))

(defn decode'''
  [^"[B" in]
  (loop [result (transient [])
         coll in]
    (if (seq coll)
      (recur (conj! result ((comp char unhexify-two-bytes) (take 2 coll))) (drop 2 coll))
      (persistent! result))))