(defproject crypto "0.0.0-SNAPSHOT"
  :description "Matasano Crytpo Challenge"
  :url "https://github.com/austinyun/experiments/clojure/crypto"
  :license {:name "ISC License"
            :url "http://www.isc.org/software/license"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:dev {:dependencies [[org.clojure/data.codec "0.1.0"]
                                  [perforate "0.3.2"]]
                   :plugins [[perforate "0.3.2"]]}}
  )