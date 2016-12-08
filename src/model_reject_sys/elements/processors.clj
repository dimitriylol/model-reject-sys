(ns model-reject-sys.elements.processors
  (:require [model-reject-sys.elements.element :refer [->Element]]
            [clojure.math.numeric-tower :refer [expt]]))

(defrecord Processor [])

(defn ->Processor [name loaded max]
  (Processor. nil
              (->Element {:rejection-prob (* 1.2 (expt 10 -4))
                          :name name
                          :loaded loaded
                          :max max})))

(def processors (list (->Processor :pr1 40 100) 
                      (->Processor :pr2 70 100)
                      (->Processor :pr3 30 100)
                      (->Processor :pr6 50 100)))
