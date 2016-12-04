(ns model-reject-sys.elements.magistrals
  (:require [model-reject-sys.elements.element :refer [->Element]]
            [clojure.math.numeric-tower :refer [expt]]))

(defrecord Magistral [])

(defn ->Magistral [name]
  (Magistral. nil (->Element {:rejection-prob (* 1.4 (expt 10 -5))
                              :name name})))

(def magistrals (map ->Magistral '(:m1)))

