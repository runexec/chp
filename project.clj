(defproject chp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.3"]
                 [garden "0.1.0-beta2"]
                 [korma "0.3.0-RC5"]
                 [lib-noir "0.5.2"]
                 [lobos "1.0.0-beta1"]
                 [postgresql/postgresql "8.4-702.jdbc4"]]
  :plugins [[lein-ring "0.8.3"]
            [lein-cljsbuild "0.3.1"]]
  :ring {:port 8000
         :auto-reload? true
         :auto-refresh? true
         :reload-paths ["src/chp/"]
         :handler chp.handler/app}
  :aliases {"schema" ["run" "-m" "chp.schema/load-schemas"]}
  :cljsbuild {:builds [{:source-paths ["resources/cljs"]
                        :compiler {:output-to "resources/public/js/main.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}})
