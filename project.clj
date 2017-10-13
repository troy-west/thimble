(defproject com.troy-west/thimble "0.0.1-SNAPSHOT"
  :description "Distributed systems in your pocket"
  :dependencies [[org.clojure/clojure "1.9.0-alpha20"]
                 [org.clojure/core.async "0.3.443"]
                 [org.clojure/tools.logging "0.3.1"]
                 [com.smxemail/ccm-clj "1.1.0" :exclusions [org.clojure/tools.logging]]
                 [org.apache.kafka/kafka_2.11 "0.11.0.1" :exclusions [org.apache.zookeeper/zookeeper org.slf4j/slf4j-log4j12 log4j/log4j org.slf4j/slf4j-simple org.jboss.netty/netty]]
                 [org.apache.kafka/kafka-clients "0.11.0.1" :exclusions [org.apache.zookeeper/zookeeper org.slf4j/slf4j-log4j12 log4j/log4j org.slf4j/slf4j-simple org.jboss.netty/netty]]
                 [org.apache.zookeeper/zookeeper "3.4.10" :exclusions [org.slf4j/slf4j-log4j12 log4j/log4j org.slf4j/slf4j-simple org.jboss.netty/netty]]
                 [org.apache.storm/storm-core "1.1.1" :exclusions [org.apache.zookeeper/zookeeper]]
                 [org.apache.storm/storm-kafka "1.1.1"]
                 [yieldbot/marceline "0.3.1-SNAPSHOT"]
                 [com.troy-west/arche "0.2.2"]
                 [cheshire "5.8.0"]]
  :init-ns [user]
  :aot [troy-west.thimble.storm.topology
        troy-west.thimble.storm.state])
