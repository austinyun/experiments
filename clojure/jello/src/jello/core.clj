(ns jello.core
  (:require [datomic.api :as d]
            [datomic.api :refer [q]]
            [jello.db-setup]))

(defonce conn (jello.db-setup/setup))

(defn transact [tx]
  (d/transact conn tx))

(defn add-player [name]
  (transact [{:db/id #db/id[:db.part/user]
              :name name
              :elo/rating 1500.0
              :elo/k 32
              }]))

(defn get-all-names []
  (q '[:find ?name
       :where [?player :name ?name]]
     (d/db conn)))

(defn get-id
  ([name]
    (ffirst (q `[:find ?player :where [?player :name ~name]]
                  (d/db conn))))

  ([name dbc]
    (ffirst (q `[:find ?player :where [?player :name ~name]]
                  dbc))))

(defn find-player [name]
  (let [dbc (d/db conn)]
    (d/entity dbc (get-id name dbc))))

(defn change-elo [name elo]
  ; TODO add an update to k-values if the rating has changed breakpoints
  (let [dbc (d/db conn)
        id (get-id name dbc)]
    (transact [{:db/id id :elo/rating elo}])))
