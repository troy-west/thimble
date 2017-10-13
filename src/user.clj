(ns user
  (:require [troy-west.thimble.zookeeper :as zk]
            [troy-west.thimble.kafka :as kafka]
            [troy-west.thimble.storm :as storm]
            [troy-west.thimble.talk :as talk]
            [troy-west.thimble.cassandra :as cassandra]))

;; clear target/zookeeper-data and target/kafka-logs prior to running init
(defn init-all
  []
  (cassandra/start-cluster)
  (let [zookeeper     (zk/start)
        kafka-server  (kafka/start zookeeper)
        storm-cluster (storm/start zookeeper)]))

(defn read-write-cassandra
  []
  (let [conn (cassandra/connection)]
    (talk/insert conn "keynote" 9)
    (talk/insert conn "keynote" 8)
    (talk/insert conn "keynote" 9)
    (talk/select conn "keynote")))

