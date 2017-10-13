(ns user
  (:require [troy-west.thimble.zookeeper :as zk]
            [troy-west.thimble.kafka :as kafka]
            [troy-west.thimble.storm :as storm]
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
    (cassandra/insert-talk conn "keynote" 9)
    (cassandra/insert-talk conn "keynote" 8)
    (cassandra/insert-talk conn "keynote" 9)
    (cassandra/select-talk conn "keynote")))

