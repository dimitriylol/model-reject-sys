(ns model-reject-sys.elements.bus
  (:require [model-reject-sys.elements.element :refer [->Element]]
            [clojure.math.numeric-tower :refer [expt]]))

(defrecord Bus [])

(defn ->Bus [name]
  (Bus. nil (->Element {:rejection-prob (* 1.4 (expt 10 -5))
                        :name name})))

(def bus (map ->Bus '(:b1 :b2 :b3)))
