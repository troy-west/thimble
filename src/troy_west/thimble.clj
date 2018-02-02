(ns troy-west.thimble
  (:require [troy-west.thimble.kafka]
            [troy-west.thimble.zookeeper]
            [troy-west.thimble.cassandra]
            [troy-west.arche.integrant]
            [troy-west.arche.hugcql]
            [integrant.core :as ig]))

(defn start
  ([]
   (start {:thimble/zookeeper.server  {}
           :thimble/kafka.broker      {:zookeeper (ig/ref :thimble/zookeeper.server)
                                       :topics    ["test-topic-1"]}
           :thimble/kafka.producer    {:broker (ig/ref :thimble/kafka.broker)}
           :thimble/cassandra.cluster {}
           :arche/cluster             (ig/ref :thimble/cassandra.cluster)
           :arche/connection          {:cluster  (ig/ref :arche/cluster)
                                       :keyspace "sandbox"}}))
  ([config]
   (ig/init config)))

(defn stop
  [state]
  (ig/halt! state))