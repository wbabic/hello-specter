(ns hello-specter.specter
  (:require
   [reagent.core :as reagent]
   [sablono.core :as sab :include-macros true]
   [com.rpl.specter :as s])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest defcard-rg defcard-doc]]))

(enable-console-print!)

(defcard spector-in-devcards
  "# Let's Explore Specter
[Specter](https://github.com/nathanmarz/specter)

See the original introductory blog post by Nathan Marz:

[Functional-navigational programming in Clojure(Script) with Specter](http://nathanmarz.com/blog/functional-navigational-programming-in-clojurescript-with-sp.html)

This page just presents those ideas using devcards as an exercise to understand devcards."
  )

(def world
  {:people [{:money 129827     :name "Alice Brown"}
            {:money 100        :name "John Smith"}
            {:money 6821212339 :name "Donald Trump"}
            {:money 2870       :name "Charlie Johnson"}
            {:money 8273821    :name "Charlie Rose"}]
   :bank {:funds 4782328748273}}
  )

(defcard world
  "Suppose a program has a data structure that contains information about a bank
and a list of customers indexed by the order in which they joined the bank.
Like this:"
  world)

(defn transfer
  "Note that this function works on *any* world structure. This handles
   arbitrary many to many transfers of a fixed amount without overdrawing anyone"
  [world from-path to-path amt]
  (let [;; Get the sequence of funds for all entities making a transfer
        givers (s/select from-path world)

        ;; Get the sequence of funds for all entities receiving a transfer
        receivers (s/select to-path world)

        ;; Compute total amount each receiver will be credited
        total-receive (* amt (count givers))

        ;; Compute total amount each transferrer will be deducted
        total-give (* amt (count receivers))]

    ;; Make sure every transferrer has sufficient funds
    (if (every? #(>= % total-give) givers)
      (->> world
           ;; Deduct from transferrers
           (s/transform from-path #(- % total-give))
           ;; Credit the receivers
           (s/transform to-path #(+ % total-receive))
           )
      (throw (js/Error. "Not enough funds!"))
      )))

(defcard-doc
  "# Transfer function

And now suppose that we wish apply a transform to transfer
an amount between customers and the bank,
without overdrawing anyone,
using Specter's select and transform. Here is a generic transform:"
  (dc/mkdn-pprint-source transfer))

;; core concepts of specter
(def v-of-m [{:a 2 :b 3} {:a 1} {:a 4}])

(comment
  (s/select [s/ALL :a even?] v-of-m)
  ;;=> [2 4]

  )

(defcard select
  "# Select values

select money of all people

```clojure
(s/select [:people s/ALL :money] world)
;;=> [129827 100 6821212339 2870 8273821]
```

select bank funds

```clojure
(s/select [:bank :funds] world)
;;=> [4782328748273]
```

")

(defcard transform-transfer
  "# Transform world

transfer 1000 dollars from bank to all people

```clojure
(transfer
  world
  [:bank :funds]
  [:people s/ALL :money]
  1000)
"
  (transfer
   world
   [:bank :funds]
   [:people s/ALL :money]
   1000))
