(ns model-reject-sys.elements.redistribute
  (:require [model-reject-sys.elements.element-dispatcher :refer [dispatcher]]
            [model-reject-sys.elements.loadable :refer [delta-load
                                                        incr-load]]))

(def processors-redistribution-table
  {:pr1 '((nil 30 nil 10) (nil nil 40 nil))
   :pr2 '((20 nil nil 50) (nil nil 20 50))
   :pr3 '()
   :pr6 '()
   })

(defmulti redistributable-on? (fn [el _] (dispatcher el)))
(defmulti redistribute (fn [el _] (dispatcher el)))

(defn apply-redistributable-rule [redistributable-rule elements]
  (every? true? (map (fn [element redistributable-val]
                       (redistributable-on? element redistributable-val))
                     elements
                     redistributable-rule)))

(defmethod redistributable-on? :processor
  [processor val]
  (if val (<= val (delta-load processor)) true))

(defmethod redistributable-on? :default
  [element val]
  false)

(defmethod redistribute :processor
  [processor processors]
  (or (some (fn [redistributable-rule]
              (when (apply-redistributable-rule redistributable-rule
                                                processors)
                (map (fn [redistributable-val processor]
                       (incr-load processor redistributable-val))
                     redistributable-rule
                     processors)))
            ((:name processor) processors-redistribution-table))
      processors))

(defmethod redistribute :default
  [_ elements]
  elements)
