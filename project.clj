(defproject bcp47 "0.1.0-SNAPSHOT"
  :description "A tool for converting the IANA BCP47 language subtag registry to JSON"
  :license {:name "MIT/X11"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [the/parsatron "0.0.2"]
                 [cheshire "4.0.2"]
                 ]
  :main bcp47.core
  )
