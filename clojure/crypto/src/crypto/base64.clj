(ns crypto.base64
  (:require [clojure.core.reducers :as r]))

;; Quick definitions
(def ^String charset "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")
(def charvec (vec charset))

;; Taken from the core.data version, minus type hints
(def ^"[B" dec-bytes
  ;; This is actually a generic way of generating a reverse lookup table
  (let [enc-bytes (.getBytes charset)
        ba (byte-array (inc (apply max enc-bytes)))]
    (doseq [[idx enc] (map-indexed vector enc-bytes)]
      (aset ba enc (byte idx)))
    ba))

(defn count-when
  "Returns # of elements of xs where pred holds"
  [pred xs]
  (count (filter pred xs)))

;; Encoding
;; This could be faster as a loop/recur structure but meh. The advice is to
;; manipulate raw byte arrays whenever possible and this stuff will only ever
;; be called while pretty printing, and it's probably I/O bound anyway.
(defn triplet->quartet
  "a 24-bit input group is formed by concatenating 3 8-bit input groups.
  These 24 bits are then treated as 4 concatenated 6-bit groups"
  [[a b c]]
  (let [n (+ (bit-shift-left a 16) (bit-shift-left b 8) c)
        w (-> n (bit-shift-right 18) (bit-and 63))
        x (-> n (bit-shift-right 12) (bit-and 63))
        y (-> n (bit-shift-right 6) (bit-and 63))
        z (-> n (bit-and 63))]
     [w x y z]))

(defn encode
  "Return a Base64 encoded string"
  [^"[B" in]
  (let [triplets (partition 3 3 (repeat 0) in)
        quartets (mapcat triplet->quartet triplets)
        pad-char \=
        pad-length (count-when zero? (last triplets))
        pad-bytes (repeat pad-length pad-char)
        encoded-bytes (->> quartets (map charvec) (drop-last pad-length))
        ]
     (apply str (concat encoded-bytes pad-bytes))))

;; Decoding
(defn quartet->triplet
  [[w x y z]]
  ;; Could move the lookup into the outer function (map lookup raw-bytes) but
  ;; doing everything a few bytes at a time seems more portable to doing things
  ;; on say, an input stream or something, and might exploit memory locality better
  ;; but I have no idea what the hell I'm talking about when it comes to performance
  (let [lookup #(aget dec-bytes %)
        w (-> w lookup (bit-shift-left 18))
        x (-> x lookup (bit-shift-left 12))
        y (-> y lookup (bit-shift-left 6))
        z (-> z lookup)
        n (+ w x y z)
        ;; Could be clever and do the next three in reverse order
        a (-> n (bit-shift-right 16) (bit-and 0xFF))
        b (-> n (bit-shift-right 8) (bit-and 0xFF))
        c (-> n (bit-and 0xFF))]
    (take-while (complement zero?) [a b c])))

(defn decode
  "Decode a Base64 encoded string"
  [^"[B" in]
  {:pre [(zero? (mod (count in) 4))]}
  (let [quartets (partition 4 4 in)
        triplets (mapcat quartet->triplet quartets)
        decoded-bytes (map char triplets)
        ]
    (apply str decoded-bytes)))

;; Alternate version using ->>

(defn decode'
  [^"[B" in]
  (letfn [(quartet->triplet
           [[w x y z]]
           (let [n (+ (bit-shift-left w 18)
                      (bit-shift-left x 12)
                      (bit-shift-left y 6)
                      z)
                 a (-> n (bit-shift-right 16) (bit-and 0xFF))
                 b (-> n (bit-shift-right 8) (bit-and 0xFF))
                 c (-> n (bit-and 0xFF))]
             [a b c]))]
    (->> in
         (map #(aget dec-bytes %))
         (partition 4 4)
         (mapcat quartet->triplet)
         (map char)
         (apply str)
         )))