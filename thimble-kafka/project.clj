(defproject com.troy-west/thimble-kafka "0.1.0-SNAPSHOT"
  :description "Thimble: Apache Kafka Testing Toolkit"

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-modules "0.3.11"]]

  :dependencies [[org.apache.kafka/kafka_2.11 "1.0.0" :exclusions [org.slf4j/slf4j-log4j12
                                                                   log4j/log4j
                                                                   org.slf4j/slf4j-simple]]])
