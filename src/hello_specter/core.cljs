(ns hello-specter.core
  (:require
   [reagent.core :as reagent]
   [sablono.core :as sab :include-macros true]
   [hello-specter.specter]
   [hello-specter.state]
   [hello-specter.commands]
   [hello-specter.programs]
   [hello-specter.svg]
   [hello-specter.turtle]
   [hello-specter.circle]
   [hello-specter.reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest defcard-rg defcard-doc]]))

(enable-console-print!)

(defcard devcards-and-reagent
  "# Devcards and reagent
This page explores using devcards with reagent

[devcards](https://github.com/bhauman/devcards),
which aims to provide a visual REPL experience for ClojureScript and

[reagent](https://reagent-project.github.io),
a minimalistic React for ClojureScript

")

(defcard reagent-no-help
  "A devcard with a reagent element:
```clojure
(defcard reagent-no-help
  (reagent/as-element [:h1 \"Reagent Example\"]))
```
"
  (reagent/as-element [:h1 "Reagent Example"]))

(defn reagent-component-example []
  [:div "reagent component example"])

(defcard reagent-no-help-2
  "
```clojure
(defn reagent-component-example []
  [:div \"reagent component example\"])

(defcard reagent-no-help
  (reagent/as-element [reagent-component-example]))
```
"
  (reagent/as-element [reagent-component-example]))

(defcard reagent-macro-1
  "
```clojure
(dc/reagent [:div \"This works fine\"])
```
"
  (dc/reagent [:div "This works fine"]))

(defcard reagent-macro-2
  "
```clojure
(dc/reagent (fn [data-atom _] [:div \"this works as well\"]))
```
"
  (dc/reagent (fn [data-atom _] [:div "this works as well"])))

(defcard-rg rg-example-2
  "using defcard-rg macro
```clojure
(defcard-rg rg-example-2
 [:div \"this works\"])
```
"
  [:div "this works"])

(defcard-rg rg-example
  "some docs"
  (fn [data-atom _] [:div "this works as well"])
  (reagent/atom {:counter 5}))

(defcard-rg jamming
  [:div {:style {:border "10px solid blue" :padding "20px"}}
   [:h1  "Composing Reagent Hiccup on the fly"]
   [:p "adding arbitrary hiccup"]])

(defn on-click [ratom]
  (swap! ratom update-in [:count] inc))

(defonce counter1-state (reagent/atom {:count 0}))

(defn counter1 []
  [:div "Current count: " (@counter1-state :count)
   [:div
    [:button {:on-click #(on-click counter1-state)}
     "Increment"]]])

(defcard-rg counter1
  [counter1]) ;; <--1

(defonce counter2-state (reagent/atom {:count 0}))

(defn counter2 []
  [:div "Current count: " (@counter2-state :count)
   [:div
    [:button {:on-click #(on-click counter2-state)}
     "Increment"]]])

(defcard-rg counter2
  [counter2] ;; <-- 1
  counter2-state ;; <-- 2
  {:inspect-data true} ;; <-- 3
  )

(defonce counter3-state (reagent/atom {:count 0}))

(defn counter3 [ratom] ;; <-- counter2 expects one argument
  [:div "Current count: " (@ratom :count)
   [:div
    [:button {:on-click #(on-click ratom)}
     "Increment"]]])

(defcard-rg counter3
  [counter3 counter3-state] ;; <-- passing in a ratom (counter3-state) to our reagent component (counter3)
  counter3-state ;; <-- notice that we are *still* passing in a 2nd argument to defcard!
  {:inspect-data true}
  )

(defonce counter4-state (reagent/atom {:count 0}))

(defn counter4 [ratom
                {:keys [title button-text]}] ;; <-- counter4 expects two arguments: a ratom, and a hash-map
  [:div [:h3 title]
   [:div "Current count: " (@ratom :count)]
   [:div [:button {:on-click #(on-click ratom)}
          button-text]]])

(defcard-rg counter4
  [counter4 counter4-state
   {:title "Counter 4"
    :button-text "increment"}] ;; <-- passing in two arguments to our reagent component
  counter4-state ;; <-- notice that we are *still* passing in a 2nd argument to defcard!
  {:inspect-data true}
  )

(defcard-rg isolating-state
  (fn [data-atom _]
    [counter4 data-atom {:title "Counter 5"
                         :button-text "increment isolated state"}])
  (reagent/atom {:count 0}) ; <-- intial ratom
  {:inspect-data true})

(defn main []
  ;; conditionally start the app based on wether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (js/React.render (sab/html [:div "This is working"]) node)))

(main)
