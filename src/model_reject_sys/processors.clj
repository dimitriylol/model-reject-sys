(ns model-reject-sys.processors)

(defn load-info [loaded max] {:loaded loaded :max max})

(defn apply-redistributable-rule [redistributable-rule loadable-elements]
  (every? true? (map (fn [loadable-element redistributable-val]
                       (redistributable-on? loadable-element redistributable-val))
                     loadable-elements
                     redistributable-rule)))

(def processors-redistribution-table
  {:pr1 '((nil 30 nil 10) (nil nil 40 nil))
   :pr2 '((20 nil nil 50) (nil nil 20 50))
   :pr3 '()
   :pr6 '()
   })

(def processors-load-table
  {:pr1 (load-info 40 100) 
   :pr2 (load-info 70 100)
   :pr3 (load-info 30 100)
   :pr6 (load-info 50 100)
   })

(defprotocol LoadableElement
  (delta-load [_])
  (redistributable-on? [_ val])
  (redistribute [_ processors])
  (incr-load [_ val]))

(defrecord Processor [name loaded max]
  LoadableElement
  (delta-load [_] (- max loaded))
  (redistributable-on? [processor val]
    (if val (<= val (delta-load processor)) true))
  (redistribute [processor processors]
    (some (fn [redistributable-rule]
         (when (apply-redistributable-rule redistributable-rule processors)
           (map (fn [redistributable-val processor] (incr-load processor redistributable-val))
                redistributable-rule
                processors)))
          ((:name processor) processors-redistribution-table)))
  (incr-load [processor val]
    (if val
      (->Processor (:name processor) (+ val (:loaded processor)) (:max processor))
      processor)))
