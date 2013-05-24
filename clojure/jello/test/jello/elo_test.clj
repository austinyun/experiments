(ns jello.elo-test
  (:use jello.elo
        midje.sweet))

(fact "The sum of two player's expected scores is approximately 1.0"
  (let [a 1600.0
        b 1400.0
        sum (reduce + (expected-score a b))]
    sum => (roughly 1.0)))

(fact "update-rating properly updates the player's rating"
  (let [player {:elo/k 32 :elo/rating 1400.0}]
    (-> (update-rating player 1500.0) :elo/rating) => 1500.0))

(fact "update-k-value properly sets k values based on ratings"
   (-> {:elo/k 32 :elo/rating 1500.0} update-k-value :elo/k) => 32
   (-> {:elo/k 32 :elo/rating 2200.0} update-k-value :elo/k) => 24
   (-> {:elo/k 32 :elo/rating 3000.0} update-k-value :elo/k) => 16)

(fact "update-scores calculates new ratings and updates k-values"
  (let [alice {:elo/k 24 :elo/rating 2399.0}
        bob   {:elo/k 16 :elo/rating 2600.0}
        new-values (update-scores {:winner alice :loser bob}) ]
    (-> new-values :winner :elo/k) => 16
    (> (-> new-values :winner :elo/rating) (alice :elo/rating)) => true
    (> (-> new-values :loser :elo/rating) (bob :elo/rating)) => false))
