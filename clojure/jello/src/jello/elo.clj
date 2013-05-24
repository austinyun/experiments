(ns jello.elo)

(defn expected-score
  "Given two ratings 'a' and 'b', calculates the expected scores for each
  and returns the vector [Ea Eb]"
  [a b]
  (let [q (fn [rating] (Math/pow 10 (/ rating 400)))
        qa (q a)
        qb (q b)
        sum (+ qa qb)]
    [(/ qa sum) (/ qb sum)]))

(defn update-k-value
  "Given a player, sets the players elo/k value to the appropriate value
  for their rating."
  [player]
  (let [rating (:elo/rating player)
        set-new-k (partial assoc player :elo/k)]
    (cond
      (< rating 2100) (set-new-k 32)
      (< rating 2400) (set-new-k 24)
      :else           (set-new-k 16))))

(defn update-rating
  "Given a player and new rating, returns the player with :elo/rating set
  to the new rating."
  [player new-rating]
  (assoc player :elo/rating new-rating))

(def update-elo (comp update-k-value update-rating))

(defn update-scores
  "Given a map of the form {:winner player1 :loser player2}, calculates
  the new :elo/rating and :elo/k values for the winning and losing players
  and returns the same map with the player's values updated."
  [{:keys [winner loser] :as match}]
    (let [a (:elo/rating winner)
          b (:elo/rating loser)
          [Ea Eb] (expected-score a b)
          delta-winner (* (:elo/k winner) (- 1 Ea))
          delta-loser (* (:elo/k loser) (- 0 Eb))
          a' (+ a delta-winner)
          b' (+ b delta-loser)]
      (assoc match :winner (update-elo winner a')
                   :loser (update-elo loser b'))))
