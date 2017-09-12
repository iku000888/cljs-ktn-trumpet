(defproject iku000888/cljs-ktn-trumpet "0.1.3"
  :description "ClojureScript Wrapper for Kintone JavaScript API"
  :url "https://github.com/iku000888/cljs-ktn-trumpet"
  :license {:name "MIT License"
            :url ""}
  :dependencies [[cljs-ajax "0.5.9"]]
  :profiles {:dev {:dependencies [[org.clojure/clojurescript "1.9.908"]
                                  [org.clojure/clojure "1.9.0-alpha17"]]}})
;; Snipet for making sure the build passes

#_ ((require '[cljs.build.api :as b])
    (b/watch "src"
             {:main 'cljs-ktn-trumpet.api
              :verbose true
              :output-to "api.js"
              :optimizations :simple}))
