(ns model-reject-sys.state-vector.generate
  (:require [model-reject-sys.state-vector.generator :refer [n-state-vectors]]
            [model-reject-sys.model :refer [init-model
                                            elements
                                            apply-state-vector
                                            probability
                                            fails-statistic]]))

(defn result-state-applying [ok-probability fails]
  {:ok-probability ok-probability :fails fails})

(defn execute-test [model state-generator]
  (reduce (fn [{ok-probability :ok-probability failed-modules :fails} state-vector]
            (if (apply-state-vector model state-vector)
              (do
                (print ".")
                (result-state-applying 
                 (+ ok-probability (probability model state-vector))
                 failed-modules))
              (do (print "x")
                  (result-state-applying ok-probability
                                         (merge-with +
                                                     failed-modules
                                                     (fails-statistic model
                                                                      state-vector))))))
          (result-state-applying 0 (fails-statistic model))
          (state-generator)))

(defn dump-res [{res-prob :ok-probability fails :fails}]
  (spit "res.log"
        (str "Probability result " res-prob "\n failed modules " fails "\n")
        :append true))

(defn execute-tests [model]
  (let [n (count (elements model))
        probability-all-works (probability model (first (n-state-vectors 0 n)))]
    (map (fn [[multiplicity required-percentile correction]]
           (println "Execute test with " multiplicity " multiplicity")
           (dump-res
            (update
             (execute-test model
                           (fn []
                             (if (= 100 required-percentile)
                               (n-state-vectors multiplicity n)
                               (n-state-vectors multiplicity n required-percentile))))
             :ok-probability
             (fn [working-probability]
               (+ probability-all-works
                  (* correction working-probability))))))
       '((1 100 1)
         (2 100 1)
         (3 50 2)
         (4 10 10)))))
