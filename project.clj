(defproject com.troy-west/thimble-all "0.1.13-SNAPSHOT"
  :description "Thimble: A Clojure toolkit for testing Streaming Data Platforms"

  :url "https://github.com/troy-west/thimble"

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v20.html"}

  :plugins [[lein-modules "0.3.11"]
            [lein-cljfmt "0.7.0" :exclusions [org.clojure/clojure
                                              com.google.errorprone/error_prone_annotations
                                              com.google.code.findbugs/jsr305]]
            [lein-kibit "0.1.8" :exclusions [org.clojure/clojure org.clojure/tools.reader]]]

  :dependencies [[com.troy-west/thimble-zookeeper "_"]
                 [com.troy-west/thimble-kafka "_"]
                 [com.troy-west/thimble-cassandra "_"]]

  :profiles {:dev      {:resource-paths ["test-resources"]
                        :dependencies   [[ch.qos.logback/logback-classic "1.2.3"]]}

             :provided {:dependencies [[org.clojure/tools.logging "_"]
                                       [integrant "_"]]}}

  :modules {:inherited {:dependencies        [[org.clojure/clojure "_"]]

                        :subprocess          nil

                        :deploy-repositories [["releases" {:url "https://clojars.org/repo/" :creds :gpg}]]

                        :aliases             {"puff" ["do"
                                                      ["clean"]
                                                      ["install"]
                                                      ["deps"]
                                                      ["check"]
                                                      ["test"]
                                                      ["kibit"]
                                                      ["cljfmt" "check"]]}}

            :versions  {org.clojure/clojure             "1.10.1"
                        org.clojure/tools.logging       "1.1.0"
                        integrant                       "0.8.0"
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

  :aliases {"smoke" ["do" ["modules" "puff"] ["clean"] ["install"] ["check"] ["kibit"] ["cljfmt" "check"]]}

  :pedantic? :abort)
