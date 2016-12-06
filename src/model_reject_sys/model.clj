(ns model-reject-sys.model
  (:require [model-reject-sys.elements.bus :refer [bus]]
            [model-reject-sys.elements.controllers :refer [controllers]]
            [model-reject-sys.elements.sensors :refer [sensors]]
            [model-reject-sys.elements.processors :refer [processors]]
            [model-reject-sys.elements.magistrals :refer [magistrals]]
            [model-reject-sys.elements.adapters :refer [adapters]]))

(defn task1 [state-vector]
  (let [interesting-elements '(:d1 :d2 :c1 :b1 :b2 :b3 :pr1 :pr2 :pr3 :a1 :a2 :m1)
        elems-state (merge (map (fn [elem-name]
                                  ({elem-name (:logical-state (elem-name state-vector))}))
                                interesting-elements))]
    (and (:d1 elems-state)
         (:d2 elems-state)
         (:c1 elems-state)
         (or (:b1 elems-state)
             (:b2 elems-state))
         (or (:pr1 elems-state)
             (:pr2 elems-state)
             (and (:a1 elems-state)
                  (:m1 elems-state)
                  (:a2 elems-state)
                  (:b3 elems-state)
                  (:pr3 elems-state))))))

(defn task2 [state-vector] true)
(defn task3 [state-vector] true)
(defn task4 [state-vector] true)

(defprotocol IModel
  (elements (_)))

(defrecord Model [])

(defn ->Model []
  (Model. nil {:bus bus
               :controllers controllers
               :sensors sensors
               :processors processors
               :magistrals magistrals
               :adapters adapters}))

(extend-type Model
  IModel
  (elements (model) (apply concat (vals model)))
  (logic-struc-func (state-vector) (and task1 task2 task3 task4)))
