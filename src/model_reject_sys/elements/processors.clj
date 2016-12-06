(ns model-reject-sys.elements.processors
  (:require [model-reject-sys.elements.element :refer [->Element]]
            [clojure.math.numeric-tower :refer [expt]]))

(defprotocol LoadableElement
  (delta-load [_])
  (redistributable-on? [_ val])
  (redistribute [_ processors])
  (incr-load [_ val]))

(defn apply-redistributable-rule [redistributable-rule loadable-elements]
  (every? true? (map (fn [loadable-element redistributable-val]
                       (redistributable-on? loadable-element redistributable-val))
                     loadable-elements
                     redistributable-rule)))

(defrecord Processor [])

(def processors-redistribution-table
  {:pr1 '((nil 30 nil 10) (nil nil 40 nil))
   :pr2 '((20 nil nil 50) (nil nil 20 50))
   :pr3 '()
   :pr6 '()
   })

(extend-type Processor
  LoadableElement
  (delta-load [processor] (- (:max processor) (:loaded processor)))
  (redistributable-on? [processor val]
    (if val (<= val (delta-load processor)) true))
  (redistribute [processor processors]
    (some (fn [redistributable-rule]
            (when (apply-redistributable-rule redistributable-rule
                                              processors)
              (map (fn [redistributable-val processor]
                     (incr-load processor redistributable-val))
                   redistributable-rule
                   processors)))
          ((:name processor) processors-redistribution-table)))
  (incr-load [processor val]
    (if val (update processor :loaded (partial + val)) processor)))


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
