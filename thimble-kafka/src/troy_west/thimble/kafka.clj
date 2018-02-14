(ns troy-west.thimble.kafka
  (:require [clojure.java.io :as io]
            [troy-west.thimble.zookeeper :as zookeeper]
            [integrant.core :as ig])
  (:import (java.util Map)
           (kafka.server KafkaServerStartable KafkaConfig)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.clients.admin AdminClient NewTopic KafkaAdminClient)))

(def default-broker-config {"host.name"                        "localhost"
                            "port"                             "9092"
                            "num.partitions"                   "12"
                            "default.replication.factor"       "1"
                            "offsets.topic.replication.factor" "1"})

(def default-producer-config {"value.serializer" "org.apache.kafka.common.serialization.StringSerializer"
                              "key.serializer"   "org.apache.kafka.common.serialization.StringSerializer"})

(defn start-broker
  [zk-state topics config]
  (let [tmp-dir (io/file (System/getProperty "java.io.tmpdir") "thimble-temp-kf")]
    (doseq [tmp-file (butlast (reverse (file-seq tmp-dir)))]
      (io/delete-file tmp-file))
    (let [config (assoc (merge default-broker-config config)
                        "zookeeper.connect" (zookeeper/server-address zk-state)
                        "log.dir" (.getPath tmp-dir))
          broker (KafkaServerStartable. (KafkaConfig. config))]
      (.startup broker)
      (let [admin-host   (str (get config "host..name") ":" (get config "port"))
            admin-client (AdminClient/create {"bootstrap.servers" admin-host})]
        (when (seq topics)
          (let [n-parts  (Integer/parseInt (get config "num.partitions"))
                r-factor (Integer/parseInt (get config "default.replication.factor"))]
            (.get (.all (.createTopics admin-client
                                       (map #(NewTopic. %1 n-parts r-factor)
                                            topics))))))
        {:config       config
         :broker       broker
         :admin-client admin-client}))))

(defn stop-broker
  [broker-state]
  (.shutdown ^KafkaServerStartable (:broker broker-state))
  (.close ^KafkaAdminClient (:admin-client broker-state)))

(defn start-producer
  [broker-state config]
  (let [broker-config  (:config broker-state)
        broker-address (str (get broker-config "host.name") ":" (get broker-config "port"))
        config         (assoc (merge default-producer-config config) "bootstrap.servers" broker-address)]
    {:config   config
     :producer (KafkaProducer. ^Map config)}))

(defn stop-producer
  [producer-state]
  (.close ^KafkaProducer (:producer producer-state)))

(defn send-message
  [producer-state topic key message]
  (.send ^KafkaProducer (:producer producer-state) (ProducerRecord. topic key message)))

(defn list-topics
  [broker-state]
  (.get (.names (.listTopics ^AdminClient (:admin-client broker-state)))))

(defmethod ig/init-key :thimble/kafka.broker
  [_ config]
  (assert (:zookeeper config))
  (start-broker (:zookeeper config) (:topics config) (:config config)))

(defmethod ig/halt-key! :thimble/kafka.broker
  [_ broker-state]
  (stop-broker broker-state))

(defmethod ig/init-key :thimble/kafka.producer
  [_ config]
  (assert (:broker config))
  (start-producer (:broker config) (:config config)))

(defmethod ig/halt-key! :thimble/kafka.producer
  [_ producer-state]
  (stop-producer producer-state))