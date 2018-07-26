(defproject com.troy-west/thimble-kafka "0.1.12-SNAPSHOT"
  :description "Thimble: Apache Kafka Toolkit"

  :url "https://github.com/troy-west/thimble"

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-modules "0.3.11"]]

  :dependencies [[com.troy-west/thimble-zookeeper "_"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]
                 [org.apache.kafka/kafka-streams "1.1.1" :exclusions [org.slf4j/slf4j-api]]
                 [org.apache.kafka/kafka_2.12 "1.1.1" :exclusions [log4j/log4j
                                                                   org.slf4j/slf4j-api
                                                                   org.slf4j/slf4j-log4j12
                                                                   org.apache.zookeeper/zookeeper]]
                 [org.apache.kafka/kafka-clients "1.1.1" :exclusions [log4j/log4j
                                                                      org.slf4j/slf4j-api
                                                                      org.slf4j/slf4j-simple
                                                                      org.slf4j/slf4j-log4j12
                                                                      org.jboss.netty/netty
                                                                      org.apache.zookeeper/zookeeper]]])