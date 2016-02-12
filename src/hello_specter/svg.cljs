(ns hello-specter.svg
  "reder turtle commands to svg"
  (:require
   [devcards.core]
   [cljs.core.match :refer-macros [match]]
   [reagent.core :as reagent]
   [sablono.core :as sab :include-macros true]
   [com.rpl.specter :as s]
   [complex.number :as n]
   [complex.vector :as v]
   [hello-specter.commands :as turtle]
   [hello-specter.programs :as programs])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest defcard-rg defcard-doc]]))

(defn config-with-resolution [res]
  {:domain [-2 2]
   :range [-2 2]
   :resolution [res res]})

(def config-1 (config-with-resolution 200))

(def round-pt (fn [p] (mapv Math.round p)))

(defn user->screen
  [config]
  (let [[xi xf] (:domain config)
        [yi yf] (:range config)
        [xres yres] (:resolution config)
        sx (/ xres (- xf xi))
        sy (/ yres (- yi yf))
        scale (v/scale sx sy)
        translate (v/translation [(- xi) (- yf)])]
    (fn [p]
      (if (number? p)
        (* sx p)
        ((comp round-pt scale translate) p)))))

(defn transform-fn [resolution]
  (let [config (config-with-resolution resolution)]
    (comp (user->screen config) n/coords)))

(def t-fn (transform-fn 200))

(comment
  (t-fn n/zero)
  ;;=> [100 100]
  )

(defn turtle-command->svg-command
  [turtle command]
  (match command
         [:forward d]
         (let [v (n/mult (:heading turtle) d)
               w (n/add (:position turtle) v)]
           [:L w])
         :else nil))

(defn process-command
  "process turtle command returning transitioned turtle and svg-command, if any"
  [turtle command]
  [(turtle/transform-turtle turtle command)
   (turtle-command->svg-command turtle command)])

(defn reducing-fn
  "process turtle command and update turtle program state,
adding svg-commands, if any"
  [program-state turtle-command]
  (let [{:keys [turtle svg-commands]} program-state
        [new-program-state svg-command] (process-command turtle turtle-command)]
    (-> program-state
        (assoc-in [:turtle] new-program-state)
        (update-in [:svg-commands] #(conj % svg-command)))))

(defn initial-program-state [turtle program]
  (let [position (:position turtle)]
    {:turtle turtle
     :turtle-program program
     :commands-processed []
     :svg-commands [[:M position]]}))

(def is (initial-program-state turtle/initial-turtle (programs/polygon 4)))

(defn svg-command->string [command t-fn]
  (match command
         [:M p]
         (let [[px py] (t-fn p)]
           (str "M " px " " py " "))
         [:L p]
         (let [[px py] (t-fn p)]
           (str "L " px " " py " "))
         :else nil))

(defn svg-path-string [svg-commands t-fn]
  (clojure.string/trim
   (clojure.string/join
    (map #(svg-command->string % t-fn) svg-commands))))

(defn commands->svg
  "screate svg component for svg-commands"
  [svg-commands resolution t-fn]
  (let [path-string (svg-path-string svg-commands t-fn)]
    [:svg {:width resolution :height resolution}
     [:path {:d path-string
             :stroke "black" :fill "white"}]]))

(defn render->svg [turtle program resolution]
  (let [is (initial-program-state turtle program)
        fs (reduce reducing-fn is program)
        t-fn (transform-fn resolution)
        path-string (svg-path-string (:svg-commands fs) t-fn)]
    [:svg {:width resolution :height resolution}
     [:path {:d path-string
             :stroke "black" :fill "white"}]]))

(defcard-doc
  "
#Render to svg

render a given turtle and a turtle program to svg.


```clojure
(render->svg turtle/initial-turtle (programs/polygon 4) 200)
```"
  (render->svg turtle/initial-turtle (programs/polygon 4) 200))

(defcard-rg turtle->svg
  "turtle program rendered to svg"
  [:div
   (render->svg turtle/initial-turtle (programs/polygon 4) 200)])

(defcard-rg pentagon
  "more turtle programs rendered to svg"
  [:div
   (render->svg turtle/initial-turtle (programs/polygon 5) 200)
   (render->svg turtle/initial-turtle (programs/sierpensky 1) 200)
   (render->svg turtle/initial-turtle (programs/star 5) 200)
   (render->svg turtle/initial-turtle (programs/spiral 10 72 0.75) 200)])


(def program-state (reagent/atom is))

(defn display-turtle
  [turtle]
  (let [{:keys [position heading]} turtle]
    {:position (n/coords position)
     :heading (n/coords heading)}))

(defn display-program
  [program]
  (into [:div] (map #(vector :p (str %)) program)))

(defn process-next-command
  "update program state by processing next command
returns updated program state"
  [program-state]
  (let [{:keys [turtle turtle-program commands-processed svg-comands]}
        program-state
        next-command (first turtle-program)
        [next-turtle svg-command] (process-command turtle next-command)]
    (-> program-state
        (assoc :turtle next-turtle)
        (update-in [:turtle-program] rest)
        (update-in [:commands-processed] #(conj % next-command))
        (update-in [:svg-commands] #(conj % svg-command)))))

(comment
  (process-next-command @program-state)
  (swap! program-state process-next-command)
  )

(defn program-state-comp [program-state-ratom]
  (let [s @program-state-ratom
        t (:turtle s)
        dt (display-turtle t)
        c (:svg-commands s)
        command-string (svg-path-string c t-fn)
        _ (println (display-turtle t))]
    [:div
     [:div
      [:h2 "turtle state"]
      [:p (str "position: " (:position dt))]
      [:p (str "heading: " (:heading dt))]]
     [:div
      [:h2 "turtle program"]
      (display-program (:turtle-program s))]
     [:div
      [:h2 "turtle commands processed"]
      (display-program (:commands-processed s))]
     [:div
      [:h2 "svg command-string"]
      [:p (str "svg: " command-string)]]
     [:div
      [:h2 "svg"]
      (commands->svg c 200 t-fn)]
     [:div
      [:h4 "program execution"]
      [:button {:on-click #(swap! program-state-ratom
                                  process-next-command)}
       "next"]
      [:button "back"]]]))

(defcard-rg program-state-card
  "
## turtle program execution

consists of:

* the turtle state
* a sequence turtle commands to process (tctp)
* a sequence turtle commands received (tcr)

a next button to process next turtle command,
which consists of:

* take next command in tctp and
* apply that command to the turtle state, generating a new one, updating
* then add that command to tcr
* use old turtle state and command to add an svg command to the svg-command sequence, if necessary
* render svg-command-sequence to svg using react
"
  [program-state-comp program-state]
  program-state
  )
