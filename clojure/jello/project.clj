(defproject jello "0.0.0-SNAPSHOT"

  :description "Ratings in Clojure"
  :url "http://github.com/austinyun/experiments/jello"
  :license {:name "ISC License"
            :url "http://www.isc.org/software/license"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.datomic/datomic-free "0.8.3960"]]
  :profiles {:dev {:plugins [[lein-midje "3.0.1"]]
                   :dependencies [[midje "1.5.1" :exclusions [org.clojure/clojure]]]}}
)