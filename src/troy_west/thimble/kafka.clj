(ns troy-west.thimble.kafka
  (:require [troy-west.thimble.zk :as zk])
  (:import (kafka.server KafkaServerStartable KafkaConfig)
           (org.apache.zookeeper.server ServerConfig)
           (java.net InetSocketAddress)))

(defn start
  [zookeeper config]
  (let [server (KafkaServerStartable.
                (KafkaConfig.
                 (merge {"zookeeper.connect"                "127.0.0.1:2181"
                         "offsets.topic.replication.factor" (short 1)
                         "log.dir"                          "target/kafka-logs"}
                        (if zookeeper
                          {"zookeeper.connect" (zk/server-address (:config zookeeper))}
                          {})
                        config)))]
    (.startup server)
    server))