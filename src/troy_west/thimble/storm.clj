(ns troy-west.thimble.storm
  (:require [troy-west.thimble.zookeeper :as zk])
  (:import (org.apache.storm LocalCluster)))

(defn start
  [zookeeper]
  (LocalCluster. (zk/server-host (:config zookeeper))
                 (zk/server-port (:config zookeeper))))