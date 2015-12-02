(ns hello-specter.programs
  (:require
   [reagent.core :as reagent]
   [sablono.core :as sab :include-macros true]
   [com.rpl.specter :as s]
   [complex.number :as n]
   [complex.geometry :as g]
   [hello-specter.commands :as c])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest defcard-rg defcard-doc]]))

(defn polygon
  "returns a program for drawing a regular polygon with n sides"
  [n]
  (c/repeat-commands
   n
   [:forward 1]
   [:turn (/ 360 n)]))

(defn star
  "returns a program to draw a star polygon"
  [n]
  (c/repeat-commands
   n
   [:forward 1]
   [:turn (/ 720 n)]))

(defn spiral
  ""
  [n a s]
  (c/repeat-commands
   n
   [:forward 1]
   [:turn a]
   [:resize s]))

;; todo: wheel,rosette

(defn sierpensky
  "program to draw sierpensky gasket to given level"
  [level]
  (if (= 0 level)
    (polygon 3)
    (c/repeat-commands
     3
     [:resize (/ 2)]
     (sierpensky (- level 1))
     [:resize 2]
     [:move 1]
     [:turn 120])))

(defcard-doc
  "
#Turtle program

A turtle program is a sequence of turtle commands."

  (dc/mkdn-pprint-source sierpensky)

  "hello-specter.programs=> (sierpensky 1)"
  (sierpensky 1)
  )
