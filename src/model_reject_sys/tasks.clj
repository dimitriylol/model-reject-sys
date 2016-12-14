(ns model-reject-sys.tasks)

(defn build-elems-state [interesting-elements state-map]
  (apply hash-map
         (mapcat (fn [elem-name]
                   (list elem-name (elem-name state-map)))
                 interesting-elements)))

(defn task1 [state-map]
  (let [interesting-elements '(:d1 :d2 :c1 :b1 :b2 :b3 :pr1 :pr2 :pr3 :a1 :a2 :m1 :pr6)
        elems-state (build-elems-state interesting-elements state-map)]
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
                  (or (:pr3 elems-state)
                      (:pr6 elems-state)))))))

(defn task2 [state-map]
  (let [interesting-elements '(:d8 :c6 :b3 :pr3 :pr6 :a2 :m1 :a1 :b1 :b2 :pr1 :pr2)
        elems-state (build-elems-state interesting-elements state-map)]
    (and (:d8 elems-state)
         (:c6 elems-state)
         (or (:pr3 elems-state)
             (:pr6 elems-state))
         (:b3 elems-state)         
         (:a2 elems-state)
         (:m1 elems-state)
         (:a1 elems-state)
         (or (:pr1 elems-state)
             (:pr2 elems-state))
         (or (:b1 elems-state)
             (:b2 elems-state)))))

(defn task3 [state-map]
  (let [interesting-elements '(:d2 :d3 :c2 :b1 :b2 :pr1 :pr2 :a1 :m1 :a2 :b3 :pr6 :pr3)
        elems-state (build-elems-state interesting-elements state-map)]
    (and (:d2 elems-state)
         (:d3 elems-state)
         (:c2 elems-state)
         (or (:b1 elems-state)
             (:b2 elems-state))
         (or (:pr1 elems-state)
             (:pr2 elems-state))
         (:a1 elems-state)         
         (:m1 elems-state)
         (:a2 elems-state)
         (:b3 elems-state)
         (or (:pr6 elems-state)
             (:pr3 elems-state)))))

(defn task4 [state-map]
  (let [interesting-elements '(:d6 :d7 :c5 :pr3 :pr6 :a2 :b3 :m1 :a1 :b1 :b2 :pr1 :pr2 :c4)
        elems-state (build-elems-state interesting-elements state-map)]
    (or
     (and (:d6 elems-state)
          (:d7 elems-state)
          (:c5 elems-state)
          (:b3 elems-state)
          (or (:pr3 elems-state)
              (:pr6 elems-state))
          (:a2 elems-state)         
          (:m1 elems-state)
          (:a1 elems-state)
          (or (:b1 elems-state)
              (:b2 elems-state))
          (or (:pr1 elems-state)
              (:pr2 elems-state)))
     (and (:d6 elems-state)
          (:c4 elems-state)
          (:m1 elems-state)
          (:a1 elems-state)
          (or (:pr1 elems-state)
              (:pr2 elems-state))
          (or (:b1 elems-state)
              (:b2 elems-state))))))

(defn tasks [elements-map]
  (every? true? (map (fn [task] (task elements-map))
                     (list task1 task2 task3 task4))))
