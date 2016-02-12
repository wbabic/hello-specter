(ns hello-specter.reagent
  (:require
   [devcards.core]
   [reagent.core :as reagent]
   [sablono.core :as sab :include-macros true])
  (:require-macros
   [reagent.ratom :refer [reaction]]
   [devcards.core :as dc :refer [defcard deftest defcard-rg defcard-doc]]))

(enable-console-print!)

;; reagent component with a canvas
(def window-width (reagent/atom nil))

(defn draw-canvas [canvas]
  (let [ctx (.getContext canvas "2d")]
    (.beginPath ctx)
    (.moveTo ctx 0 0)
    (.lineTo ctx 300 300)
    (.moveTo ctx 300 0)
    (.lineTo ctx 0 300)
    (.stroke ctx)))

(defn div-with-canvas []
  (let [dom-node (reagent/atom nil)]
    (reagent/create-class
     {:component-did-update
      (fn [this]
        (println "component did update")
        (draw-canvas (.-firstChild @dom-node)))
      :component-did-mount
      (fn [this]
        (println "component did mount")
        (reset! dom-node (reagent/dom-node this)))
      :component-will-unmount
      (fn [this]
        (println "component will unmount"))
      :reagent-render
      (fn []
        @window-width
        [:div.with-canvas
         [:canvas (if-let [node @dom-node]
                    {:width 300
                     :height 300})]])})))

(defn canvas-component []
  [div-with-canvas])

(defcard-doc
  "
### Canvas in a reagent component

create a react class using reagent/create-class and define

* store the dom node in an ratom in component-did-mount

* use that node to get a context in component-did-update

* use the context to draw into the canvas
")

(defcard-rg canvas-component-card
  "A reagent component with a canvas"
  canvas-component)
