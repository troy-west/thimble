(ns troy-west.thimble.zookeeper
  (:require [integrant.core :as ig]
            [clojure.java.io :as io])
  (:import (org.apache.zookeeper.server ZooKeeperServer ServerCnxnFactory)
           (org.apache.zookeeper.server.persistence FileTxnSnapLog)))

(def default-config {"tickTime"          3000
                     "clientPort"        2181
                     "maxClientCnxns"    30
                     "minSessionTimeout" -1
                     "maxSessionTimeout" -1})

(defn start
  [config]
  (let [tmp-dir (io/file (System/getProperty "java.io.tmpdir") "thimble-temp-zk")]
    (doseq [tmp-file (butlast (reverse (file-seq tmp-dir)))]
      (io/delete-file tmp-file))
    (let [config     (merge default-config config)
          server     (ZooKeeperServer.)
          tx-log     (FileTxnSnapLog. (io/file tmp-dir) (io/file tmp-dir))
          cx-factory (ServerCnxnFactory/createFactory (int (get config "clientPort"))
                                                      (int (get config "maxClientCnxns")))]
      (.setTickTime server (int (get config "tickTime")))
      (.setMinSessionTimeout server (int (get config "minSessionTimeout")))
      (.setMaxSessionTimeout server (int (get config "maxSessionTimeout")))
      (.setTxnLogFactory server tx-log)
      (.startup cx-factory server)
      {:config     config
       :tx-log     tx-log
       :cx-factory cx-factory
       :server     server})))

(defn stop
  [state]
  (.shutdown ^ZooKeeperServer (:server state) true)
  (.close ^FileTxnSnapLog (:tx-log state))
  (.shutdown ^ServerCnxnFactory (:cx-factory state)))

(defn server-address
  [state]
  (let [config (:config state)]
    (str "localhost:" (get config "clientPort"))))

(defmethod ig/init-key :thimble/zookeeper.server
  [_ config]
  (start (:config config)))

(defmethod ig/halt-key! :thimble/zookeeper.server
  [_ state]
  (stop state))