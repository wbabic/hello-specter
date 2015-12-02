(defproject hello-specter "0.1.0-SNAPSHOT"
  :description "an exploritory project using devcards and reagent"
  :url "http://wbabic.github.io"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.reader "1.0.0-alpha1"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.clojure/core.async  "0.2.371"]

                 [devcards "0.2.1"]
                 [sablono "0.4.0"]
                 [reagent "0.5.1"]
                 [com.rpl/specter "0.8.0"]

                 [prismatic/schema "1.0.3"]
                 [complex/complex "0.1.9"]

                 [org.clojure/core.match "0.3.0-alpha4"]

                 [thi.ng/geom "0.0.908"]
                 [thi.ng/color "1.0.0"]

                 [clj-time "0.9.0"]
                 [ring/ring-core "1.4.0"]]

  :plugins [[lein-cljsbuild "1.1.1" :exclusions [org.clojure/clojure]]
            [lein-figwheel "0.5.0-1" :exclusions [ring/ring-core
                                                  joda-time
                                                  org.clojure/clojure
                                                  org.clojure/tools.reader]]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"]

  :source-paths ["src"]

  :cljsbuild {
              :builds [{:id "devcards"
                        :source-paths ["src"]
                        :figwheel { :devcards true } ;; <- note this
                        :compiler { :main       "hello-specter.core"
                                    :asset-path "js/compiled/devcards_out"
                                    :output-to  "resources/public/js/compiled/hello_specter_devcards.js"
                                    :output-dir "resources/public/js/compiled/devcards_out"
                                    :source-map-timestamp true }}
                       {:id "dev"
                        :source-paths ["src"]
                        :figwheel true
                        :compiler {:main       "hello-specter.core"
                                   :asset-path "js/compiled/out"
                                   :output-to  "resources/public/js/compiled/hello_specter.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :source-map-timestamp true }}
                       {:id "prod"
                        :source-paths ["src"]
                        :compiler {:main       "hello-specter.core"
                                   :asset-path "js/compiled/out"
                                   :output-to  "resources/public/js/compiled/hello_specter.js"
                                   :optimizations :advanced}}]}

  :figwheel { :css-dirs ["resources/public/css"] })
