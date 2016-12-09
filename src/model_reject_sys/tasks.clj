(ns model-reject-sys.tasks)

(defn task1 [state-map]
  (let [interesting-elements '(:d1 :d2 :c1 :b1 :b2 :b3 :pr1 :pr2 :pr3 :a1 :a2 :m1)
        elems-state (apply hash-map
                           (mapcat (fn [elem-name]
                                     (list elem-name (elem-name state-map)))
                                   interesting-elements))]
    (and (:d1 elems-state)
         (:d2 elems-state)
         (:c1 elems-state)
         (or (:b1 elems-state)
             (:b2 elems-state))
         (or (:pr1 elems-state)
             (:pr2 elems-state)
             (and (:a1 elems-state)
                  (:m1 elems-state)
                  (:a2 elems-state)
                  (:b3 elems-state)
                  (:pr3 elems-state))))))

(defn task2 [state-vector] true)
(defn task3 [state-vector] true)
(defn task4 [state-vector] true)

(defn tasks [elements-map]
  (every? true? (map (fn [task] (task elements-map)) (list task1 task2 task3 task4))))
