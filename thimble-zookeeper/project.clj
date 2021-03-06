(defproject com.troy-west/thimble-zookeeper "0.1.13-SNAPSHOT"
  :description "Thimble: Apache Zookeeper Toolkit"

  :url "https://github.com/troy-west/thimble"

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-modules "0.3.11"]]

  :dependencies [[org.apache.zookeeper/zookeeper "3.6.2" :exclusions [org.slf4j/slf4j-api
                                                                      org.slf4j/slf4j-log4j12
                                                                      org.jboss.netty/netty]]])
