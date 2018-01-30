(ns troy-west.thimble.zookeeper
  (:require [integrant.core :as ig])
  (:import (java.util Properties)
           (org.apache.zookeeper.server.quorum QuorumPeerConfig)
           (org.apache.zookeeper.server ServerConfig ZooKeeperServerMain)))

(def default-config {"dataDir"           "target/zookeeper-data"
                     "tickTime"          "2000"
                     "clientPortAddress" "127.0.0.1"
                     "clientPort"        "2181"})

(defn start
  [config]
  (let [props         (doto (Properties.)
                        (.putAll (merge default-config config)))
        quorum-config (doto (QuorumPeerConfig.)
                        (.parseProperties props))
        config        (doto (ServerConfig.)
                        (.readFrom quorum-config))
        server        (ZooKeeperServerMain.)]
    (future (.runFromConfig server config))
    {:config config
     :server server}))

(defn stop
  [{:keys [server]}]
  (let [shutdown (.getDeclaredMethod ZooKeeperServerMain "shutdown" (into-array Class []))]
    (.setAccessible shutdown true)
    (.invoke shutdown server (into-array []))))

(defmethod ig/init-key :thimble.zookeeper/server
  [_ config]
  (start config))

(defmethod ig/halt-key! :thimble.zookeeper/server
  [_ server]
  (stop server))