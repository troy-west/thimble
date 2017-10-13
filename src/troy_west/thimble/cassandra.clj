(ns troy-west.thimble.cassandra
  (:require [ccm-clj.core :as ccm]
            [troy-west.arche :as arche]
            [troy-west.thimble.talk :as talk]
            [qbits.alia :as alia])
  (:import (java.util Date)))

;; Prior to running this, install ccm-clj (https://github.com/SMX-LTD/ccm-clj) and run:
;;   # ccm create -n 3 -v 2.0.14 thimble

(defn start-cluster
  []
  (ccm/auto-cluster! "thimble"
                     "2.0.14"
                     3
                     [#"schema/keyspace\.cql"]
                     {"sandbox" [#"schema/tables\.cql"]}
                     {:cql 19142}))

(defn connection
  []
  (let [cluster (alia/cluster {:contact-points ["localhost"]
                               :port           19142})]
    (arche/connect cluster
                   {:keyspace   "sandbox"
                    :statements talk/statements})))
