(ns hello-specter.turtle
  (:require
   [devcards.core]
   [reagent.core :as reagent]
   [sablono.core :as sab :include-macros true]
   [com.rpl.specter :as s])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest defcard-rg defcard-doc]]))


(def turtle
  {:points
   {:zero 'zero
    :one 'one
    :i 'i
    :infinity 'infinity
    :negative-one 'negative-one
    :negative-i 'negative-i}
   :circles
   {:x-axis [:zero :one :infinity]
    :y-axis [:zero :i :infinity]
    :unit-circle [:one :i :negative-one]}
   :style
   {:circles
    {:x-axis      {:edge :red   :inside :lt-red  }
     :y-axis      {:edge :green :inside :lt-green}
     :unit-circle {:edge :blue  :inside :lt-blue }}}})

(defcard hello-turtle
  "# Introduction to turtle graphics
Turtle consists of six points represented by complex numbers
and three generalized circles.

Each circle passes through four points.
Each of the circles intersect the other two circles in exactly two points.

In addition to geometry, this turtle has style.

We can express the structure of the turtle in a map, like this:"
  turtle)

(defcard turtle-select
  "
## Turtle Selections

select x-axis

```clojure
(s/select [:circles :x-axis] turtle)
;;=> [[:zero :one :infinity]]
```

select i
```clojure
(s/select [:points :i] turtle)
;;=> [i]
```


select three circles
```clojure
(s/select [:circles s/ALL (s/srange 1 2)] turtle)
;;=> [[[:zero :one :infinity]] [[:zero :i :infinity]] [[:one :i :negative-one]]]
```

select six points
```clojure
(s/select [:points s/ALL (s/srange 1 2)] turtle)
;;=> [[zero] [one] [i] [infinity] [(negative one)] [(negative i)]]
```

```clojure
(s/select [:points s/ALL (s/srange 1 2) s/ALL] turtle)
;;=> [zero one i infinity (negative one) (negative i)]
```

")

(defcard turtle-transform
  "## Turtle Transforms

### Connecting geometry to algebra,

#### using complex number:

* translation by addition

* dilation and rotation by multiplication

* reflection in x-axis by taking the conjugate

* reciprocation in unit circle by taking the reciproal

* Mobius tranformation by composing the above transforms

Later, we will see how the Mobius transformations are actually the circle preserving tranformations.
")

(defn transform-number
  "pretend number transformation funtion for purpose of illustration"
  [function]
  (fn [k] (symbol (str function "_" (name k)))))

(defcard transform-points
  "## Transform turtle geometry

### leave style the same

```clojure
(defn transform-number
  \"pretend number transformation function for purpose of illustration\"
  [function]
  (fn [k] (symbol (str function \"_\" (name k)))))

(let [t-fn (transform-number \"f\")]
    (s/transform [:points s/ALL (s/srange 1 2) s/ALL] t-fn turtle))
```

the point keywords now reference the transformed numbers

the circles now refer to the transformed points:
"
  (let [t-fn (transform-number "f")]
    (s/transform [:points s/ALL (s/srange 1 2) s/ALL] t-fn turtle)))

(comment
  ;; into this namespace for repl play and exploration
  (in-ns 'hello-specter.turtle)
  )


(defn point-transform [t-fn turtle]
  (s/transform [:points s/ALL (s/srange 1 2) s/ALL] t-fn turtle))

(comment
  ((transform-number "f") 'one)
  (let [t-fn (transform-number "f")]
    (point-transform t-fn turtle))
  ;;=>
  {:points
   {:zero f_zero,
    :one f_one,
    :i f_i,
    :infinity f_infinity,
    :negative-one f_negative-one,
    :negative-i f_negative-i},
   :circles
   {:x-axis [:zero :one :infinity],
    :y-axis [:zero :i :infinity],
    :unit-circle [:one :i :negative-one]},
   :style
   {:circles {:x-axis
              {:edge :red, :inside :lt-red},
              :y-axis {:edge :green, :inside :lt-green},
              :unit-circle {:edge :blue, :inside :lt-blue}}}}

  ;; transform by f and then by g
  (let [t-fn1 (transform-number "f")
        t-fn2 (transform-number "g")]
    (point-transform t-fn2
                     (point-transform t-fn1 turtle)))
  ;;=>
  {:points
   {:zero g_f_zero,
    :one g_f_one,
    :i g_f_i,
    :infinity g_f_infinity,
    :negative-one g_f_negative-one,
    :negative-i g_f_negative-i},
   :circles
   {:x-axis [:zero :one :infinity],
    :y-axis [:zero :i :infinity],
    :unit-circle [:one :i :negative-one]},
   :style
   {:circles
    {:x-axis {:edge :red, :inside :lt-red},
     :y-axis {:edge :green, :inside :lt-green},
     :unit-circle {:edge :blue, :inside :lt-blue}}}}
  )
