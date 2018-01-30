(ns troy-west.thimble.zookeeper
  (:import (java.util Properties)
           (org.apache.zookeeper.server.quorum QuorumPeerConfig)
           (org.apache.zookeeper.server ServerConfig ZooKeeperServerMain)))

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
    quorum-config))

(defn server-config
  [^QuorumPeerConfig config]
  (let [server-config (ServerConfig.)]
    (.readFrom server-config config)
    server-config))

(defn server-host
  [^ServerConfig config]
  (.getHostString (.getClientPortAddress config)))

(defn server-port
  [^ServerConfig config]
  (long (.getPort (.getClientPortAddress config))))

(defn server-address
  [^ServerConfig config]
  (str (server-host config) ":" (server-port config)))

(defn start
  ([]
   (start {"tickTime"          "2000"
           "clientPortaddress" "127.0.0.1"
           "clientPort"        "2181"
           "dataDir"           "target/zookeeper-data"}))
  ([config]
   (let [config (-> config properties quorum-config server-config)
         server (ZooKeeperServerMain.)]
     (future (.runFromConfig server config))
     {:config config
      :server server})))
