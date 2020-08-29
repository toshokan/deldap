(defproject deldap "0.1.0-SNAPSHOT"
  :description "Delegated LDAP administration"
  :url "https://github.com/toshokan/deldap"
  :license {:name "MIT" }
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [aero "1.1.6"]
                 [compojure "1.6.2"]
                 [mount "0.1.16"]
                 [org.clojars.pntblnk/clj-ldap "0.0.16"]
                 [ring-cors "0.1.13"]
                 [ring/ring-core "1.8.1"]
                 [ring/ring-json "0.5.0"]
                 [ring/ring-jetty-adapter "1.8.1"]]
  :main ^:skip-aot deldap.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
