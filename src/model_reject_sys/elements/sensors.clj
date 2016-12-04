(ns model-reject-sys.elements.sensors
  (:require [model-reject-sys.elements.element :refer [->Element]]
            [clojure.math.numeric-tower :refer [expt]]))

(defrecord Sensor [])

(defn ->Sensor [name]
  (Sensor. nil (->Element {:rejection-prob (* 3.2 (expt 10 -5))
                           :name name})))

(def sensors (map ->Sensor '(:d1 :d2 :d3 :d6 :d7 :d8)))
