(ns troy-west.thimble.cassandra
  (:require [clojure.java.io :as io]
            [ccm-clj.core :as ccm]
            [integrant.core :as ig]))

(def default-config {:name         "thimble"
                     :version      "3.0.15"
                     :nodes        3
                     :port         19142
                     :keyspace     "sandbox"
                     :keyspace-cql "schema/sandbox-keyspace.cql"})

(defn start-cluster
  [config]
  (let [config (merge default-config config)
        {:keys [name version nodes port keyspace keyspace-cql schema-cqls]} config]
    (ccm/stop!)
    (when (ccm/cluster? name) (ccm/remove! name))
    (ccm/new! name version nodes {:cql port})
    (ccm/start! name)
    (ccm/cql! (io/resource keyspace-cql))
    (doseq [cql schema-cqls]
      (ccm/cql! (io/resource cql) keyspace))
    {:contact-points ["127.0.0.1"]
     :port           (:port config)}))

(defmethod ig/init-key :thimble/cassandra.cluster
  [_ config]
  (start-cluster config))

(defmethod ig/halt-key! :thimble/cassandra.cluster
  [_ _]
  (ccm/stop!))