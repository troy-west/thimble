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
  (let [config        (merge default-config config)
        props         (doto (Properties.) (.putAll config))
        quorum-config (doto (QuorumPeerConfig.) (.parseProperties props))
        server-config (doto (ServerConfig.) (.readFrom quorum-config))
        server        (ZooKeeperServerMain.)]
    (deref (future (.runFromConfig server server-config)) 6000 :await)
    {:config config
     :server server}))

(defn stop
  [state]
  (let [shutdown (.getDeclaredMethod ZooKeeperServerMain "shutdown" (into-array Class []))]
    (.setAccessible shutdown true)
    (.invoke shutdown (:server state) (into-array []))))

(defn server-address
  [state]
  (let [config (:config state)]
    (str (get config "clientPortAddress") ":" (get config "clientPort"))))

(defmethod ig/init-key :thimble/zookeeper.server
  [_ config]
  (start config))

(defmethod ig/halt-key! :thimble/zookeeper.server
  [_ state]
  (stop state))