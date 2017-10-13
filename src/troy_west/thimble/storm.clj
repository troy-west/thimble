(ns troy-west.thimble.storm
  (:require [troy-west.thimble.zookeeper :as zk]
            [troy-west.thimble.storm.topology :as topology])
  (:import (org.apache.storm LocalCluster)))

(defn start
  [zookeeper]
  (LocalCluster. (zk/server-host (:config zookeeper))
                 (zk/server-port (:config zookeeper))))

(defn deploy-topology
  ([storm zookeeper]
   (deploy-topology storm zookeeper "talks"))
  ([storm zookeeper topic]
   (compile 'troy-west.thimble.storm)
   (topology/submit-topology! storm
                              "thimble"
                              (zk/server-address (:config zookeeper))
                              topic)))