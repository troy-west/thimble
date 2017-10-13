# Thimble

Distributed systems, shrunk to the REPL.

## Usage

## Cassandra Interop (requires [CCM-CLJ](https://github.com/SMX-LTD/ccm-clj))
```clojure
(require '[troy-west.thimble.cassandra :as cassandra])

(cassandra/start-cluster)
=> nil

(def conn (cassandra/connection))
=> #'user/conn

conn
=>
{:session #object[com.datastax.driver.core.SessionManager 0x25d94779 "com.datastax.driver.core.SessionManager@25d94779"],
 :statements #:demo{:select-talk {:cql "SELECT * FROM talk WHERE id = :id",
                                  :prepared #object[com.datastax.driver.core.DefaultPreparedStatement
                                                    0x5d2d0de2
                                                    "com.datastax.driver.core.DefaultPreparedStatement@5d2d0de2"]},
                    :insert-talk {:cql "INSERT INTO talk (id, rating, date) VALUES (:id, :rating, :date)",
                                  :prepared #object[com.datastax.driver.core.DefaultPreparedStatement
                                                    0x1f7fc5f9
                                                    "com.datastax.driver.core.DefaultPreparedStatement@1f7fc5f9"]}},
 :udt-encoders {}}

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

```clojure
(require '[troy-west.thimble.zk :as zk])

(def zookeeper (zk/start))
=>
#'user/zookeeper

zookeeper
=>
{:config #object[org.apache.zookeeper.server.ServerConfig
                 0x58c2a7a0
                 "org.apache.zookeeper.server.ServerConfig@58c2a7a0"],
 :server #object[org.apache.zookeeper.server.ZooKeeperServerMain
                 0x1dc022dc
                 "org.apache.zookeeper.server.ZooKeeperServerMain@1dc022dc"]}

```

## License

Copyright © 2017 Troy-West Pty. Ltd.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
