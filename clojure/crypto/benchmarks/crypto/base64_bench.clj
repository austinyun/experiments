(ns crypto.base64-bench
  (:require [perforate.core :refer :all]
            [crypto.base64 :refer :all]
            [clojure.data.codec.base64 :as b64]))

(def ^String odyssey (slurp "resources/odyssey.txt"))
(def ^"[B"   o-bytes (.getBytes odyssey))
(def ^"[B"   odyssey-encoded (b64/encode o-bytes))

(comment
(defgoal encode "Test encoding speed")
(defcase encode :reference
  [] (b64/encode o-bytes))
(defcase encode :my-version
  [] (ascii->base64 o-bytes))
)
(comment
(defgoal decode "Test decoding speed")
(defcase decode :reference
  [] (b64/decode odyssey-encoded))
(defcase decode :my-version-1
  [] (base64->ascii odyssey-encoded))
(defcase decode :my-version-2
  [] (base64->ascii' odyssey-encoded))
  )
