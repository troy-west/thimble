# Thimble

A Clojure toolkit for testing Streaming Data Platforms

## Modules

* [com.troy-west.arche/thimble-all](https://github.com/troy-west/thimble)

  [![Clojars Project](https://img.shields.io/clojars/v/com.troy-west/thimble-all.svg)](https://clojars.org/com.troy-west/thimble-all)

  Initialize and interact with a streaming data platform (Zookeeper, Kafka, Cassandra) from the REPL

* [com.troy-west.arche/thimble-zookeeper](https://github.com/troy-west/thimble/tree/master/thimble-zookeeper)

  [![Clojars Project](https://img.shields.io/clojars/v/com.troy-west/thimble-zookeeper.svg)](https://clojars.org/com.troy-west/thimble-zookeeper)

  Initialize Zookeeper from the REPL 

* [com.troy-west.arche/thimble-kafka](https://github.com/troy-west/thimble/tree/master/thimble-kafka)

  Initialize and interact with Kafka from the REPL

* [com.troy-west.arche/thimble-cassandra](https://github.com/troy-west/thimble/tree/master/thimble-cassandra)

  [![Clojars Project](https://img.shields.io/clojars/v/com.troy-west/thimble-cassandra.svg)](https://clojars.org/com.troy-west/thimble-cassandra)

  Initialize and interact with Cassandra from the REPL

## Usage

```bash
brew install ccm

;; if using thimble-all, you may need to install modules (execute cmd from project root)
lein modules install
```

```clojure
(require '[troy-west.thimble :as thimble])
=> nil

(def platform (thimble/start))
=> #'user/platform

platform
=>
{:thimble/zookeeper.server {:config {"tickTime" 3000,
                                     "clientPort" 2181,
                                     "maxClientCnxns" 30,
                                     "minSessionTimeout" -1,
                                     "maxSessionTimeout" -1},
                            :tx-log #object[org.apache.zookeeper.server.persistence.FileTxnSnapLog
                                            0x7ca7e10d
                                            "org.apache.zookeeper.server.persistence.FileTxnSnapLog@7ca7e10d"],
                            :cx-factory #object[org.apache.zookeeper.server.NIOServerCnxnFactory
                                                0x332309d6
                                                "org.apache.zookeeper.server.NIOServerCnxnFactory@332309d6"],
                            :server #object[org.apache.zookeeper.server.ZooKeeperServer
                                            0x8560b3e
                                            "org.apache.zookeeper.server.ZooKeeperServer@8560b3e"]},
 :thimble/kafka.broker {:config {"host.name" "localhost",
                                 "port" "9092",
                                 "num.partitions" "12",
                                 "default.replication.factor" "1",
                                 "offsets.topic.replication.factor" "1",
                                 "zookeeper.connect" "localhost:2181",
                                 "log.dir" "/var/folders/gz/7g238rvd6j1c_jrqqc87_7_m0000gn/T/thimble-temp-kf"},
                        :broker #object[kafka.server.KafkaServerStartable
                                        0x4b3a2cb0
                                        "kafka.server.KafkaServerStartable@4b3a2cb0"],
                        :admin-client #object[org.apache.kafka.clients.admin.KafkaAdminClient
                                              0x598a8fb2
                                              "org.apache.kafka.clients.admin.KafkaAdminClient@598a8fb2"]},
 :thimble/kafka.producer {:config {"value.serializer" "org.apache.kafka.common.serialization.StringSerializer",
                                   "key.serializer" "org.apache.kafka.common.serialization.StringSerializer",
                                   "bootstrap.servers" "localhost:9092"},
                          :producer #object[org.apache.kafka.clients.producer.KafkaProducer
                                            0x72cc50ec
                                            "org.apache.kafka.clients.producer.KafkaProducer@72cc50ec"]},
 :thimble/cassandra.cluster {:contact-points ["127.0.0.1"], :port 19142},
 :arche/cluster #object[com.datastax.driver.core.Cluster 0x4a7baa70 "com.datastax.driver.core.Cluster@4a7baa70"],
 :arche/connection {:session #object[com.datastax.driver.core.SessionManager
                                     0x4d9fd2ed
                                     "com.datastax.driver.core.SessionManager@4d9fd2ed"],
                    :statements {},
                    :udt-encoders {}}}

;; use :thimble/kafka.broker -> :admin-client to perform Kafka admin actions (list topics, create topics, etc)
;; use :thimble/kafka.produer to write to Kafka
;; use :arche/connection to read/write Cassandra
;; see: thimble/start for an example Integrant configuration, change as required
;; see: com.troy-west/arche for details on initializing the Cassandra connection and Prepared Statements

(thimble/stop platform)
```



## License

Copyright © 2017 Troy-West Pty. Ltd.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
