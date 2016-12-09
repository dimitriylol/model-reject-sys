(ns model-reject-sys.elements.adapters
  (:require [model-reject-sys.elements.element :refer [->Element]]
            [clojure.math.numeric-tower :refer [expt]]))

(defrecord Adapter [])

(defn ->Adapter [name]
  (Adapter. nil (->Element {:rejection-prob (* 1.1 (expt 10 -4))
                            :name name})))

(def adapters (map ->Adapter '(:a1 :a2)))

