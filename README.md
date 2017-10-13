# Thimble

Distributed systems, shrunk to the REPL.

The script below demonstrates the following multi-system data flow

 - start a range of distributed services from the REPL
 - send data to kafka
 - have that data processed by storm (kafka streams / onyx to follow shortly)
 - read that data back from cassandra

Default settings:

 - 12 Kafka partitions
 - 3 Storm workers
 
## Usage

## Setup

Install CCM-CLJ and manually create the first cluster (can do from the REPL, simpler this way..)

```bash
# ccm create -n 3 -v 2.0.14 thimble
```

Currently must clean up any stale zookeeper / kafka state

```bash
# rm -rf target/zookeeper-data
# rm -rf target/kafka-logs
```

## Cassandra (requires [CCM-CLJ](https://github.com/SMX-LTD/ccm-clj))

Start Cassandra and insert / select data

```clojure
(require '[troy-west.thimble.cassandra :as cassandra])

(cassandra/start-cluster)
=> nil

(def conn (cassandra/connection))
=> #'user/conn

(talk/select conn "keyspace")
=> ()

(talk/insert conn "keynote" 9)
=> ()

(talk/insert conn "keynote" 8)
=> ()

(talk/insert conn "keynote" 9)
=> ()

(cassandra/select-talk conn "keynote")
=>
({:id "keynote", :rating 8, :date #inst"2017-10-13T06:36:09.249-00:00"}
 {:id "keynote", :rating 9, :date #inst"2017-10-13T06:36:06.294-00:00"}
 {:id "keynote", :rating 9, :date #inst"2017-10-13T06:36:12.590-00:00"})
```

## Zookeeper

Start Zookeeper (require for later services)

```clojure
(require '[troy-west.thimble.zookeeper :as zk])

(def zookeeper (zk/start))
=>
#'user/zookeeper
```

## Kafka

Start Kafka and sent a message to the broker

```clojure
(require '[troy-west.thimble.kafka :as kafka])

(def server (kafka/start zookeeper))
=> #'user/server

(def producer (kafka/producer server))
=> #'user/producer

(kafka/send-message producer "scaling" 11)
=>
```

## Storm

Start storm, process events from the prior Kafka topic

```clojure
(require '[troy-west.thimble.storm :as storm ])

(def cluster (storm/start zookeeper))
=>

(storm/deploy-topology cluster zookeeper)
```

## Confirm 

```clojure
(talk/select conn "scaling")
=> ({:id "scaling", :rating 11, :date #inst"2017-10-13T22:43:55.897-00:00"})
```

## License

Copyright © 2017 Troy-West Pty. Ltd.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
