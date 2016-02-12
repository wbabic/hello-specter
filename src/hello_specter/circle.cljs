(ns hello-specter.circle
  (:require
   [devcards.core]
   [reagent.core :as reagent]
   [sablono.core :as sab :include-macros true]
   [com.rpl.specter :as s]
   [complex.number :as n]
   [complex.geometry :as g])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest defcard-rg defcard-doc]]))

(enable-console-print!)

(defcard circles-overview
  "# Circles

there are many ways to think of a circle

* a map like, {:center [0 0] :radius 1}

* a vector of three points
(which may be collinear) as in
[one i negative-one]
or [zero one infinity]

* a function of a paramater, say t
with values z_0 z_1 z_infinity for t equal to 0 1 and infinity, respectfully

* the set of all points equidistant from a center

* an equation involving z and z conjugate:
z * z-bar + a * z + a-bar * z-bar + gamma = zero

* in the form (at + b)/(ct + d)

* can be calibrated

* as something that has an edge, an inside and an outside
")


;; first lets define a circle by three points

(defn three-point-circle [c]
  (let [[z1 z2 z3] c]
    {:three-points [z1 z2 z3]}))

(defn add-param [three-p-c]
  (assoc three-p-c
         :param
         (apply g/circle (:three-points three-p-c))))

(defn add-circum-circle [three-p-c]
  (assoc three-p-c
         :circum-circle
         (g/circumcircle (:three-points three-p-c))))

(def circle-1 (-> [n/one n/i n/negative-i]
                  three-point-circle
                  add-circum-circle
                  ;;add-param
                  ))

(defcard circle-card
  "
## standard form

given by a map with center and radius keywords

## three points

a circle given by the circumcircle of three points in the plane
may be a line if the points are collinear

## parameterized three point

a circle given by a function of a parameter, say t
where the function passes through the three points
in order at t = 0, 1 and :infinity
"
  circle-1)
