(ns model-reject-sys.model
  (:require [model-reject-sys.tasks :refer [tasks]]
            [model-reject-sys.elements.element :refer [el-probability]]
            [model-reject-sys.elements.redistribute :refer [redistribute]]
            [model-reject-sys.elements.bus :refer [bus]]
            [model-reject-sys.elements.controllers :refer [controllers]]
            [model-reject-sys.elements.sensors :refer [sensors]]
            [model-reject-sys.elements.processors :refer [processors]]
            [model-reject-sys.elements.magistrals :refer [magistrals]]
            [model-reject-sys.elements.adapters :refer [adapters]]))

(defprotocol IModel
  (groups [_])
  (elements [_])
  (apply-state-vector [_ state-vector])
  (probability [_ state-vector])
  (fails-statistic [_] [_ state-vector]))

(defrecord Model [])

(defn ->Model [bus controllers sensors processors magistrals adapters]
  (Model. nil {:bus bus
               :controllers controllers
               :sensors sensors
               :processors processors
               :magistrals magistrals
               :adapters adapters}))

(defn split-by-size [lst lst-with-size]
  "split list on sub lists. Size of size sub-lists are set in lst-with-size"
  (if (and (not-empty lst) (not-empty lst-with-size))
    (cons (take (first lst-with-size) lst)
          (split-by-size (drop (first lst-with-size) lst) (rest lst-with-size)))
    nil))

(defn physical-state-apply [state-vector model]
  "return elements groups with applied state-vector to physical and logical state on each element"
  (map (fn [group-element group-phys-state]
         (map (fn [element phys-state] (assoc element
                                              :phys-state phys-state
                                              :logic-state phys-state))
              group-element
              group-phys-state))
       (groups model)
       (split-by-size state-vector
                      (map (fn [group-name] (count (get model group-name)))
                           (keys model)))))

(defn redistribute-group [index group]
  "return redistributed group of elements"
  (if (< index (count group))
    (redistribute-group
     (inc index)
     (if (false? (:logic-state (nth group index)))
       (redistribute (nth group index) group)
       group))
    group))

(defn logical-state-apply [grouped-elems]
  "return elements map with logical state of elements"
  (apply hash-map
         (mapcat (fn [{name :name log-st :logic-state}] (list name log-st))
                 (mapcat (partial redistribute-group 0) grouped-elems))))

(extend-type Model
  IModel
  (groups [model] (vals model))
  (elements [model] (apply concat (vals model)))
  (apply-state-vector [model state-vector]
    (tasks (logical-state-apply (physical-state-apply state-vector model))))
  (probability [model state-vector]
    (reduce (fn [res element] (* res (el-probability element))) 1 (elements model)))
  (fails-statistic [model]
    (apply hash-map (mapcat #(list (:name %) 0) (elements model))))
  (fails-statistic [model state-vector]
    (apply hash-map (mapcat #(list (:name %1) (if %2 0 1))
                            (elements model)
                            state-vector))))

(defn init-model [] (->Model bus controllers sensors processors magistrals adapters))
