(defproject com.troy-west/thimble-cassandra "0.1.12-SNAPSHOT"
  :description "Thimble: Apache Cassandra Toolkit"

  :url "https://github.com/troy-west/thimble"
  
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-modules "0.3.11"]]

  :dependencies [[com.troy-west/arche-hugcql "0.4.4" :exclusions [org.slf4j/slf4j-api]]
                 [com.troy-west/arche-integrant "0.4.4" :exclusions [org.slf4j/slf4j-api]]
                 [com.smxemail/ccm-clj "1.1.0" :exclusions [org.clojure/tools.logging]]])
