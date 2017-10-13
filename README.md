# Thimble

Distributed systems, shrunk to the REPL.

## Usage

```clojure
(cassandra/start-cluster)
=> nil
02:35:31.911 INFO  [nREPL-worker-1] ccm-clj.core – Creating new cluster thimble (this may take a while), listening on 19142
02:35:32.303 INFO  [nREPL-worker-1] ccm-clj.core – Added node node1 @ 127.0.0.1:19142
02:35:32.524 INFO  [nREPL-worker-1] ccm-clj.core – Added node node2 @ 127.0.0.2:19142
02:35:32.756 INFO  [nREPL-worker-1] ccm-clj.core – Added node node3 @ 127.0.0.3:19142
02:35:32.885 INFO  [nREPL-worker-1] ccm-clj.core – Switched active cluster to thimble
02:35:38.872 INFO  [nREPL-worker-1] ccm-clj.core – Cluster thimble started
02:35:39.062 INFO  [nREPL-worker-1] ccm-clj.core – Loading keyspace cqls [#object[java.net.URL 0x19a7762c file:/Users/derek/Troy-West/projects/thimble/resources/schema/keyspace.cql]]
02:35:39.063 INFO  [nREPL-worker-1] ccm-clj.core – Loading cql: file:/Users/derek/Troy-West/projects/thimble/resources/schema/keyspace.cql
02:35:39.493 INFO  [nREPL-worker-1] ccm-clj.core – Found [#object[java.net.URL 0x21038945 file:/Users/derek/Troy-West/projects/thimble/resources/schema/talks.cql]]
02:35:39.493 INFO  [nREPL-worker-1] ccm-clj.core – Loading cql: file:/Users/derek/Troy-West/projects/thimble/resources/schema/talks.cql
=> nil
(def conn (cassandra/connection))
02:35:50.663 INFO  [nREPL-worker-1] c.d.driver.core.GuavaCompatibility – Detected Guava < 19 in the classpath, using legacy compatibility layer
02:35:50.828 INFO  [nREPL-worker-1] c.datastax.driver.core.ClockFactory – Using native clock to generate timestamps.
02:35:51.074 INFO  [nREPL-worker-1] com.datastax.driver.core.NettyUtil – Detected shaded Netty classes in the classpath; native epoll transport will not work properly, defaulting to NIO.
02:35:51.353 INFO  [nREPL-worker-1] com.datastax.driver.core.Cluster – Cannot connect with protocol version V4, trying with V3
02:35:51.362 INFO  [nREPL-worker-1] com.datastax.driver.core.Cluster – Cannot connect with protocol version V3, trying with V2
02:35:51.403 WARN  [nREPL-worker-1] com.datastax.driver.core.Cluster – You listed localhost/0:0:0:0:0:0:0:1:19142 in your contact points, but it wasn't found in the control host's system.peers at startup
02:35:51.451 INFO  [nREPL-worker-1] c.d.d.c.p.DCAwareRoundRobinPolicy – Using data-center name 'datacenter1' for DCAwareRoundRobinPolicy (if this is incorrect, please provide the correct datacenter name with DCAwareRoundRobinPolicy constructor)
02:35:51.452 INFO  [nREPL-worker-1] com.datastax.driver.core.Cluster – New Cassandra host localhost/127.0.0.1:19142 added
02:35:51.452 INFO  [nREPL-worker-1] com.datastax.driver.core.Cluster – New Cassandra host /127.0.0.2:19142 added
02:35:51.452 INFO  [nREPL-worker-1] com.datastax.driver.core.Cluster – New Cassandra host /127.0.0.3:19142 added
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

## License

Copyright © 2017 Troy-West Pty. Ltd.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
