(ns jello.db-setup
  (:require [datomic.api :as d]))

(defn setup
  "Setup the datomic database."
  ([]
   (setup "datomic:mem://players"))
  ([uri]
   (d/create-database uri)
   (let [conn (d/connect uri)]
     (d/transact conn (read-string (slurp "schema.dtm")))
     conn)))