(ns troy-west.thimble.kafka
  (:require [integrant.core :as ig])
  (:import (kafka.server KafkaServerStartable KafkaConfig)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (java.util Map)))

(def default-broker-config {"log.dir"                          "target/kafka-logs"
                            "zookeeper.connect"                "127.0.0.1:2181"
                            "host.name"                        "127.0.0.1"
                            "port"                             "9092"
                            "num.partitions"                   "12"
                            "offsets.topic.replication.factor" "1"})

(def default-producer-config {"value.serializer"  "org.apache.kafka.common.serialization.StringSerializer"
                              "key.serializer"    "org.apache.kafka.common.serialization.StringSerializer"
                              "bootstrap.servers" "127.0.0.1:9092"})

(defn start-broker
  [config]
  (let [broker (KafkaServerStartable. (KafkaConfig. (merge default-broker-config config)))]
    (.startup broker)
    broker))

(defn stop-broker
  [broker]
  (.shutdown ^KafkaServerStartable broker))

(defn start-producer
  [config]
  (KafkaProducer. ^Map (merge default-producer-config config)))

(defn stop-producer
  [producer]
  (.close ^KafkaProducer producer))

(defn send-message
  [producer topic key message]
  (.send (:thimble.kafka/producer producer) (ProducerRecord. topic key message)))

(defmethod ig/init-key :thimble.kafka/broker
  [_ config]
  (start-broker config))

(defmethod ig/halt-key! :thimble.kafka/broker
  [_ broker]
  (stop-broker broker))

(defmethod ig/init-key :thimble.kafka/producer
  [_ config]
  (start-producer config))

(defmethod ig/halt-key! :thimble.kafka/producer
  [_ broker]
  (stop-producer broker))