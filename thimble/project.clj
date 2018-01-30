(defproject com.troy-west/thimble "0.1.0-SNAPSHOT"
  :description "Thimble: Streaming Platform Testing Toolkit"

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-modules "0.3.11"]]

  :dependencies [[com.troy-west/thimble-kafka "_"]
                 [com.troy-west/thimble-cassandra "_"]
                 [com.troy-west/thimble-zookeeper "_"]])
