(ns pages.core
  (:require
   [devcards.core]
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

(devcards.core/start-devcard-ui!)
