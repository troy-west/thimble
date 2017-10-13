(ns troy-west.thimble.storm.state
  (:require [troy-west.thimble.cassandra :as cassandra])
  (:import (org.apache.storm.trident.state State)))

(defrecord TridentState [cassandra]
  State
  (beginCommit [_ _])
  (commit [_ _]))

(defn initialize
  []
  (->TridentState (cassandra/connection)))
