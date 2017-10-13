# Thimble

Distributed systems, shrunk to the REPL.

## Usage

## Cassandra Interop (requires [CCM-CLJ](https://github.com/SMX-LTD/ccm-clj))
```clojure
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

## License

Copyright © 2017 Troy-West Pty. Ltd.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
