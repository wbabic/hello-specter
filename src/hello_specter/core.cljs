(ns hello-specter.core
  (:require
   [devcards.core]
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

(defn main []
  ;; conditionally start the app based on wether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (js/React.render (sab/html [:div "This is working"]) node)))

(main)
