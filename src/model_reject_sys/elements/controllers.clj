(ns model-reject-sys.elements.controllers
  (:require [model-reject-sys.elements.element :refer [->Element]]
            [clojure.math.numeric-tower :refer [expt]]))

(defrecord Controller [])

(defn ->Controller [name]
  (Controller. nil (->Element {:rejection-prob (* 1.9 (expt 10 -4))
                               :name name})))

(def controllers (map ->Controller '(:c1 :c2 :c4 :c5 :c6)))
