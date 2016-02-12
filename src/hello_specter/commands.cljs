(ns hello-specter.commands
  (:require
   [devcards.core]
   [reagent.core :as reagent]
   [sablono.core :as sab :include-macros true]
   [complex.number :as n])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest defcard-rg defcard-doc]]))

(def initial-turtle
  {:position n/zero
   :heading n/one})

(defn display-turtle
  [turtle]
  (let [{:keys [position heading]} turtle]
    {:position (n/coords position)
     :heading (n/coords heading)}))

(defn repeat-commands
  "returns sequence of commands repeated n times"
  [n & commands]
  (apply concat (repeat n commands)))

(comment
  (display-turtle initial-turtle)
  )

(defmulti transform first)

(defmethod transform :move [c turtle]
  (let [d (c 1)
        heading (:heading turtle)]
    (update-in turtle [:position] #(n/add % (n/mult heading d)))))

(defmethod transform :forward [c turtle]
  (let [d (c 1)
        heading (:heading turtle)]
    ;; set stroke to black, then back to clear, when done
    (update-in turtle [:position] #(n/add % (n/mult heading d)))))

(defmethod transform :turn [c turtle]
  (let [a (c 1)
        w (n/complex-polar a)]
    (update-in turtle
               [:heading]
               #(n/mult w %))))

(defmethod transform :resize [c turtle]
  (let [s (c 1)]
    (update-in turtle
               [:heading]
               #(n/mult % s))))

(defn transform-turtle
  "apply transform to turtle
returning a new turtle"
  [turtle transforms]
  (cond
    (vector? transforms)
    (transform transforms turtle)
    (seq? transforms)
    (reduce
     (fn [turtle transform] (transform-turtle turtle transform))
     turtle
     transforms)))

(comment
  (let [transform [:move 1]]
    (display-turtle
     (transform-turtle
      initial-turtle
      transform)))

  (let [t [:move 1]]
    (-> initial-turtle
        (transform-turtle t)
        display-turtle))
  )

(defcard-doc
  "
#Implementation of Turtle Commands

[:forward d] -> move turtle forward d steps in direction of heading, drawing a line

[:move d]    -> same as above but with out drawing a line

[:turn a]    -> change heading by rotating it by angle a given in degrees

[:resize s]  -> change step size by factor s
"

  "initial turtle:"
  (display-turtle initial-turtle)

  "
```clojure
(display-turtle (transform-turtle initial-turtle (repeat-commands 4 [:move 1] [:turn 90])))
```
"
  (display-turtle (transform-turtle initial-turtle (repeat-commands 4 [:move 1] [:turn 90]))))
