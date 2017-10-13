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

(zk/start)
=>
{:config #object[org.apache.zookeeper.server.ServerConfig 0xe2c302e "org.apache.zookeeper.server.ServerConfig@e2c302e"],
 :server #object[org.apache.zookeeper.server.ZooKeeperServerMain
                 0x5b0ab9a8
                 "org.apache.zookeeper.server.ZooKeeperServerMain@5b0ab9a8"]}
```

## License

Copyright © 2017 Troy-West Pty. Ltd.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
