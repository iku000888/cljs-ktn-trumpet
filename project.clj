(defproject iku000888/cljs-ktn-trumpet "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[cljs-ajax "0.5.9"]]
  :profiles {:dev {:dependencies [[org.clojure/clojurescript "1.9.908"]
                                  [org.clojure/clojure "1.9.0-alpha17"]]}})
;; Snipet for making sure the build passes
;; (require '[cljs.build.api :as b])
;; (b/watch "src"
;;          {:main 'cljs-ktn-trumpet.api
;;           :verbose true
;;           :output-to "api.js"
;;           :optimizations :simple})
