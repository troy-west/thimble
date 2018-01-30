(ns troy-west.thimble
  (:require [troy-west.thimble.kafka]
            [troy-west.thimble.zookeeper]
            [troy-west.thimble.cassandra]
            [integrant.core :as ig]))

(defn init
  []
  (ig/init {:thimble/zookeeper.server  {}
            :thimble/kafka.broker      {:zookeeper (ig/ref :thimble/zookeeper.server)}
            :thimble/kafka.producer    {:broker (ig/ref :thimble/kafka.broker)}
            :thimble/cassandra.cluster {}}))

(defmethod ig/init-key :thimble/platform
  [_ config]
  (init config))

(defmethod ig/halt-key! :thimble/platform
  [_ _]
  )