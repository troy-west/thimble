(defproject com.troy-west/thimble-all "0.1.6-SNAPSHOT"
  :description "Thimble: A Clojure toolkit for testing Streaming Data Platforms"

  :url "http://www.troy-west.com/thimble"

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-modules "0.3.11"]
            [lein-cljfmt "0.5.7" :exclusions [org.clojure/clojure]]
            [jonase/eastwood "0.2.5" :exclusions [org.clojure/clojure]]
            [lein-kibit "0.1.6" :exclusions [org.clojure/clojure org.clojure/tools.reader]]]

  :dependencies [[org.clojure/clojure "_"]
                 [com.troy-west/thimble-zookeeper "_"]
                 [com.troy-west/thimble-kafka "_"]
                 [com.troy-west/thimble-cassandra "_"]]

  :profiles {:dev {:resource-paths ["test-resources"]
                   :dependencies   [[ch.qos.logback/logback-classic "1.2.3"]]}}

  :modules {:inherited {:dependencies        [[org.clojure/clojure "_"]
                                              [org.clojure/tools.logging "_"]
                                              [integrant "_"]]

                        :subprocess          nil

                        :deploy-repositories [["releases" {:url "https://clojars.org/repo/" :creds :gpg}]]

                        :aliases             {"puff" ["do" ["clean"] ["install"] ["deps"] ["check"] ["test"] ["kibit"] ["cljfmt" "check"]]}

                        :eastwood            {:add-linters [:unused-fn-args
                                                            :unused-locals
                                                            :unused-namespaces
                                                            :unused-private-vars]
                                              :namespaces  [:source-paths]}}

            :versions  {org.clojure/clojure             "1.9.0"
                        org.clojure/tools.logging       "0.4.0"
                        integrant                       "0.6.3"
                        com.troy-west/thimble-zookeeper :version
                        com.troy-west/thimble-kafka     :version
                        com.troy-west/thimble-cassandra :version}}

  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["modules" "change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["modules" "deploy"]
                  ["deploy"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["modules" "change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]]

  :aliases {"smoke" ["do" ["modules" "puff"] ["clean"] ["install"] ["check"] ["kibit"]]}

  :eastwood {:add-linters [:unused-fn-args
                           :unused-locals
                           :unused-namespaces
                           :unused-private-vars]
             :namespaces  [:source-paths]}

  :pedantic? :abort)
