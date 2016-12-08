(ns model-reject-sys.elements.element-dispatcher
  (:require model-reject-sys.elements.processors)  
  (:import [model_reject_sys.elements.processors Processor]))

(defn dispatcher [el]
  (cond
    (instance? Processor el) :processor)
  )
