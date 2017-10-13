(ns troy-west.thimble.zk
  (:import (java.util Properties)
           (org.apache.zookeeper.server ServerConfig ZooKeeperServerMain)
           (org.apache.zookeeper.server.quorum QuorumPeerConfig)))

(defn properties
  [config]
  (let [props (Properties.)]
    (.putAll props (merge {"dataDir"           "target/zookeeper-data"
                           "tickTime"          "2000"
                           "clientPortAddress" "127.0.0.1"
                           "clientPort"        "2181"}
                          config))
    props))

(defn quorum-config
  [properties]
  (let [quorum-config (QuorumPeerConfig.)]
    (.parseProperties quorum-config properties)
    properties))

(defn server-config
  [quorum-config]
  (let [server-config (ServerConfig.)]
    (.readFrom server-config quorum-config)))

(defn start!
  ([]
   (start! {"tickTime"          "2000"
            "clientPortaddress" "127.0.0.1"
            "clientPort"        "2181"
            "dataDir"           "target/zookeeper-data"}))
  ([config]
   (let [config (-> config properties quorum-config server-config)
         server (ZooKeeperServerMain.)]
     (future (.runFromConfig server config))
     {:config config
      :server server})))
