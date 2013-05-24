(ns jello.core-test
  (:use jello.core
        midje.sweet))

(background
  (before :contents (do
                      (add-player "Alice")
                      (add-player "Bob"))))

(fact "A database was made and connection established"
  (str (class conn)) => "class datomic.peer.LocalConnection")

(fact "add-person adds people to the db"
  (get-all-names) => contains "Alice" "Bob")

(fact "Alice exists"
  (find-player "Alice") =not=> nil?)

(fact "Alice has initial values"
  (keys (find-player "Alice")) => contains "name" "elo/rating" "elo/k")
