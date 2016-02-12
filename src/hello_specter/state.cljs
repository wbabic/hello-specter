(ns hello-specter.state
  (:require
   [devcards.core]
   [reagent.core :as reagent]
   [sablono.core :as sab :include-macros true]
   [complex.number :as n]
   [complex.geometry :as g])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest defcard-rg defcard-doc]]))

(defcard-doc
  "
#The State of the Turtle

The standard turtle, from the LOGO programming language, is a simple creature.
It knows where it is, its position,
and where it is headed, its heading."

  {:position 'zero
   :heading 'one}

  "
Position is a point and heading is a vector.
A point has an x and y coordinates,
while a vector has a direction and a magnitude.

A step is the magnitude of the heading.

There are four turtle commands that can affect the state of the turtle.

"
)
