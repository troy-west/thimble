(ns troy-west.thimble.kafka
  (:require [troy-west.thimble.zk :as zk]
            [cheshire.core :as json])
  (:import (kafka.server KafkaServerStartable KafkaConfig)
           (org.apache.kafka.common.serialization StringSerializer)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (java.net InetSocketAddress)))

(defn server-address
  [server]
  (let [config (.serverConfig server)]
    (str (.hostName config) ":" (.port config))))

(defn start
  ([zookeeper]
   (start zookeeper {"zookeeper.connect"                "127.0.0.1:2181"
                     "num.partitions"                   (int 12)
                     "host.name"                        "127.0.0.1"
                     "port"                             "9092"
                     "offsets.topic.replication.factor" (short 1)
                     "log.dir"                          "target/kafka-logs"}))
  ([zookeeper config]
   (let [server (KafkaServerStartable.
                 (KafkaConfig. (assoc config
                                      "zookeeper.connect"
                                      (zk/server-address (:config zookeeper)))))]
     (.startup server)
     server)))

(defn producer
  [server]
  (KafkaProducer. {"value.serializer"  StringSerializer
                   "key.serializer"    StringSerializer
                   "bootstrap.servers" (server-address server)}))

(defn send-message
  [producer id rating]
  (let [message (json/encode {:id id :rating rating})
        record  (ProducerRecord. "talks" id message)]
    (.send producer record)))

