(ns model-reject-sys.elements.loadable
  (:require [model-reject-sys.elements.element-dispatcher :refer [dispatcher]]))

(defmulti delta-load dispatcher)
(defmulti incr-load (fn [el _] (dispatcher el)))

(defmethod delta-load :processor [processor] (- (:max processor) (:loaded processor)))

(defmethod delta-load :default [element] 0)

(defmethod incr-load :processor [processor val]
  (if val
    (update processor :loaded (partial + val))
    (assoc processor :logic-state true)))

(defmethod incr-load :default [element val] element)
