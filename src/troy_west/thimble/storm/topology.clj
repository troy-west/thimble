(ns troy-west.thimble.storm.topology
  (:require [marceline.storm.trident :as trident]
            [troy-west.thimble.storm.state :as state]
            [troy-west.arche :as arche]
            [cheshire.core :as json])
  (:import java.nio.charset.Charset
           [org.apache.storm.kafka.trident OpaqueTridentKafkaSpout TridentKafkaConfig]
           org.apache.storm.kafka.ZkHosts
           [org.apache.storm.spout Scheme SchemeAsMultiScheme]
           org.apache.storm.StormSubmitter
           org.apache.storm.trident.TridentTopology
           org.apache.storm.tuple.Fields)
  (:gen-class))

(def ^:const value-field "kafka-value")
(def charset-utf8 (Charset/forName "utf-8"))

(deftype RawValueScheme []
  Scheme
  (deserialize
    [_ value]
    [value])

  (getOutputFields
    [_]
    (Fields. [value-field])))

(trident/defstatefactory state-factory {:params []}
                         [_ _ part-idx num-parts]
                         (state/initialize))

(defn parse-event
  [buffer]
  (json/decode (str (.decode (.newDecoder charset-utf8) buffer))))

(trident/defstateupdater digest
                         [state tuples _]
                         (let [{:keys [cassandra]} state]
                           (doseq [event (map #(parse-event (trident/get % value-field)) tuples)]
                             (arche/execute cassandra :talk/insert {:values event}))))

(defn ->spout
  [address topic]
  (let [conf (TridentKafkaConfig. (ZkHosts. address) topic)]
    (set! (.scheme conf) (SchemeAsMultiScheme. (->RawValueScheme)))
    (set! (.ignoreZkOffsets conf) true)
    (OpaqueTridentKafkaSpout. conf)))

(defn ->topology
  [topology-name address topic]
  (let [topology (TridentTopology.)
        state    (state-factory)
        stream   (trident/new-stream topology topology-name (->spout address topic))]
    (-> (trident/partition-persist stream state [value-field] digest)
        (trident/parallelism-hint 3))
    (.build topology)))

(defn submit-topology!
  [cluster topology-name address topic]
  (.submitTopology cluster
                   topology-name
                   {"topology.debug"   false
                    "topology.workers" 3}
                   (->topology topology-name address topic)))

(defn kill-topology!
  [cluster topology-name]
  (.killTopology cluster topology-name))

