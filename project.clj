(defproject hello-specter "0.1.1"
  :description "an exploritory project using devcards and reagent"
  :url "http://wbabic.github.io/hello-specter"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [org.clojure/tools.reader "1.0.0-alpha2"]
                 [org.clojure/tools.analyzer.jvm "0.6.9"]

                 [devcards "0.2.1-6" :exclusions [org.clojure/tools.reader]]
                 [sablono "0.6.0"]
                 [reagent "0.5.1"]
                 [com.rpl/specter "0.8.0"]

                 [prismatic/schema "1.0.5"]
                 [complex/complex "0.1.9"]

                 [org.clojure/core.match "0.3.0-alpha4"]
                 [org.clojure/core.async  "0.2.374"]

                 [thi.ng/geom "0.0.908"]
                 [thi.ng/color "1.0.0"]

                 [clj-time "0.9.0"]
                 [ring/ring-core "1.4.0"]]

  :plugins [[lein-cljsbuild "1.1.2" :exclusions [org.clojure/clojure]]
            [lein-figwheel "0.5.0-6" :exclusions [ring/ring-core
                                                  joda-time
                                                  org.clojure/clojure
                                                  org.clojure/tools.reader]]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "resources/public/js/pages"
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
                       {:id "pages"
                        :source-paths ["src" "pages-src"]
                        :compiler {:main       "pages.core"
                                   :asset-path "js/pages/out"
                                   :output-to  "resources/public/js/pages/devcards.js"
                                   :optimizations :advanced}}]}

  :figwheel { :css-dirs ["resources/public/css"] })
