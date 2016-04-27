(ns hello-specter.examples
  "clojure west 2016 talk by Nathan Marz"
  (:require
   [devcards.core]
   [reagent.core :as reagent]
   [sablono.core :as sab :include-macros true]
   [com.rpl.specter :as s])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest defcard-rg defcard-doc]]))




(comment
  (in-ns 'hello-specter.examples)

  (s/transform [s/ALL s/LAST]
               inc
               {:a 1 :b 2 :c 3})

  (def MAP-VALS (s/comp-paths s/ALL s/LAST))
  (s/transform MAP-VALS
               inc
               {:a 1 :b 2 :c 3})

  (s/transform [s/ALL MAP-VALS]
               dec
               '({:a 2 :b 0} {:a 3}))

  ;; map of maps
  (s/transform [MAP-VALS MAP-VALS]
               inc
               {:a {:x 1 :y 2} :b {:x 3}})

  ;; map of vector of maps
  (s/transform [MAP-VALS s/ALL MAP-VALS]
               inc
               {:a [{:a 1} {:x 2 :y 3}] :b [{:x 3}]})

  ;; map of sets
  (def DATA
    {:a #{2 3}
     :b #{4 5}})

  ;; clojure
  (update
   DATA
   :a
   (fn [s]
     (if s (conj s 1) #{1})))

  ;; specter
  (s/transform [:a s/NIL->SET]
             (fn [s] (conj s 1))
             DATA)

  (s/transform [:a (s/subset #{})]
               (fn [_] #{1})
               DATA)
  (s/setval [:a (s/subset #{})]
            #{1}
            DATA)

  (s/setval (s/srange 4 9)
            [:hello :world]
            [0 1 2 3 4 5 6 7 8 9 10 11])

  (s/transform (s/srange 4 9)
            reverse
            [0 1 2 3 4 5 6 7 8 9 10 11])

  (s/transform [(s/srange 4 9) s/ALL even?]
               #(* 100 %)
               [0 1 2 3 4 5 6 7 8 9 10 11])

  (s/transform (s/filterer even?)
               reverse
               [0 1 2 3 4 5 6 7 8 9 10 11])

  (s/transform [(s/srange 4 13) (s/filterer even?)]
               reverse
               [0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15])

  (s/select [s/ALL :a even?]
            [{:a 1} {:a 2 :b 3} {:a 4 :c 2}])

  (s/select [(s/srange 4 9) s/ALL even?]
            [0 1 2 3 4 5 6 7 8 9 10 11])
  )
