# Thimble

Distributed systems, shrunk to the REPL.

## Usage

## Cassandra (requires [CCM-CLJ](https://github.com/SMX-LTD/ccm-clj))

Start Cassandra and insert / select data

```clojure
(require '[troy-west.thimble.cassandra :as cassandra])
=> nil

(cassandra/start-cluster)
=> nil

(def conn (cassandra/connection))
=> #'user/conn

(cassandra/select-talk conn "keynote")
=> ()

(cassandra/insert-talk conn "keynote" 9)
=> ()

(cassandra/insert-talk conn "keynote" 8)
=> ()

(cassandra/insert-talk conn "keynote" 9)
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
=> nil

(def zookeeper (zk/start))
=>
#'user/zookeeper
```

## Kafka

Start Kafka and sent a message to the broker

```clojure
(require '[troy-west.thimble.kafka :as kafka])
=> nil

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
=> nil

(def cluster (storm/start zookeeper))
=>

(storm/deploy-topology cluster zookeeper)
```

## License

Copyright © 2017 Troy-West Pty. Ltd.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
