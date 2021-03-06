(ns troy-west.thimble.kafka
  (:require [clojure.java.io :as io]
            [troy-west.thimble.zookeeper :as zookeeper]
            [integrant.core :as ig]
            [clojure.tools.logging :as log])
  (:import (java.util Map)
           (java.io File)
           (kafka.server KafkaServerStartable KafkaConfig)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (org.apache.kafka.clients.admin AdminClient NewTopic KafkaAdminClient)))

(def default-broker-config {"host.name"                        "localhost"
                            "port"                             "9092"
                            "num.partitions"                   "12"
                            "default.replication.factor"       "1"
                            "offsets.topic.replication.factor" "1"})

(def default-producer-config {"value.serializer" "org.apache.kafka.common.serialization.StringSerializer"
                              "key.serializer"   "org.apache.kafka.common.serialization.StringSerializer"})

(def default-consumer-config {"value.deserializer" "org.apache.kafka.common.serialization.StringDeserializer"
                              "key.deserializer"   "org.apache.kafka.common.serialization.StringDeserializer"})

(defn empty-dir!
  [dir]
  (doseq [tmp-file (butlast (reverse (file-seq dir)))]
    (io/delete-file tmp-file))
  dir)

(defn start-broker
  [zk-state topics dir config]
  (let [config (assoc (merge default-broker-config config)
                      "zookeeper.connect" (zookeeper/server-address zk-state)
                      "log.dir" (.getAbsolutePath ^File dir))
        broker (KafkaServerStartable. (KafkaConfig. config))]
    (.startup broker)
    (let [admin-host   (str (get config "host.name") ":" (get config "port"))
          admin-client ^AdminClient (AdminClient/create ^Map {"bootstrap.servers" admin-host})]
      (when (seq topics)
        (let [n-parts  (Integer/parseInt (get config "num.partitions"))
              r-factor (Short/parseShort (get config "default.replication.factor"))]
          (try
            (.get (.all (.createTopics admin-client (map #(NewTopic. ^String %1 n-parts r-factor) topics))))
            (catch Exception ex
              (log/error ex "Error creating topic")))))
      {:config       config
       :broker       broker
       :admin-client admin-client})))

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

(defn start-consumer
  [broker-state config]
  (let [broker-config  (:config broker-state)
        broker-address (str (get broker-config "host.name") ":" (get broker-config "port"))
        config         (assoc (merge default-consumer-config config) "bootstrap.servers" broker-address)]
    {:config   config
     :consumer (KafkaConsumer. ^Map config)}))

(defn stop-consumer
  [consumer-state]
  (.close ^KafkaConsumer (:consumer consumer-state)))

(defn send-message
  [producer-state topic key message]
  (.send ^KafkaProducer (:producer producer-state) (ProducerRecord. topic key message)))

(defn list-topics
  [broker-state]
  (.get (.names (.listTopics ^AdminClient (:admin-client broker-state)))))

(defmethod ig/init-key :thimble/kafka.broker
  [_ {:keys [zookeeper topics dir config]}]
  (assert zookeeper)
  (let [dir (if-not dir
              (-> (System/getProperty "java.io.tmpdir")
                  (io/file "thimble-temp-kf")
                  empty-dir!)
              (let [dir (io/file dir)]
                (.mkdirs dir)
                dir))]
    (start-broker zookeeper topics dir config)))

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

(defmethod ig/init-key :thimble/kafka.consumer
  [_ config]
  (assert (:broker config))
  (start-consumer (:broker config) (:config config)))

(defmethod ig/halt-key! :thimble/kafka.consumer
  [_ consumer-state]
  (stop-consumer consumer-state))
