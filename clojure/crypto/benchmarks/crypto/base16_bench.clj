(ns crypto.base16-bench
  (:require [perforate.core :refer :all]
            [crypto.base16 :as b16]))

(def ^String odyssey (slurp "resources/odyssey.txt"))
(def ^"[B"   o-bytes (.getBytes odyssey))
(def ^"[B"   o-encoded (->> o-bytes b16/encode (apply str) .getBytes))

(defgoal encode "Test encoding speed")
(defcase encode :mine
  [] (b16/encode o-bytes))

(comment
(defgoal decode "Test decoding speed")
(defcase decode :into
  [] (b16/decode o-encoded))
(defcase decode :fold-into-vec
  [] (b16/decode' o-encoded))
(defcase decode :foldcat
  [] (b16/decode'' o-encoded))
(defcase decode :loop-transient
  [] (b16/decode''' o-encoded))
  )