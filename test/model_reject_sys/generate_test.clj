(ns model-reject-sys.generate-test
  (:require [clojure.test :refer :all]
            [model-reject-sys.elements.processors :refer [->Processor]]
            [model-reject-sys.elements.bus :refer [->Bus]]
            [model-reject-sys.model :refer [->Model
                                            ;; apply-state-vector
                                            probability
                                            fails-statistic
                                            elements]]
            [model-reject-sys.state-vector.generate :refer :all]))

;; WTF!! this test is passed if execute-test was cider-debug-defun-at-point.
;; Otherwise with-redefs isn't captured
(deftest execute-test-test
  (let [model (->Model (list (->Bus :b1) (->Bus :b2)) nil nil
                       (list (->Processor :pr1-test 10 30)
                             (->Processor :pr2-test 20 40)
                             (->Processor :pr3-test 30 40)) nil nil)]    
    (with-redefs-fn {#'model-reject-sys.model/apply-state-vector (fn [_ state-vector] state-vector)
                     #'probability (fn [_ state-vector] (if state-vector 0.8 0.2))
                     #'fails-statistic (fn
                                         ([model] (apply hash-map (mapcat #(list (:name %) 0)
                                                                          (elements model))))
                                         ([model state-vector]
                                          (apply hash-map (mapcat #(list (:name %)
                                                                         (if state-vector 11 22))
                                                                  (elements model)))))}
      #(is (= {:ok-probability 1.6 :fails {:b1 0 :b2 0 :pr1-test 0 :pr2-test 0 :pr3-test 0}}
             (execute-test model (fn [_] '(true true)))))
      ;; (is (= {:ok-probability 0.8 :fails {:b1 22 :b2 22 :pr1-test 22 :pr2-test 22 :pr3-test 22}}
      ;;        (execute-test model (fn [_] '(false true)))))
      ;; (is (= {:ok-probability 0.8 :fails {:b1 22 :b2 22 :pr1-test 22 :pr2-test 22 :pr3-test 22}}
      ;;        (execute-test model (fn [_] '(true false)))))
      ;; (is (= {:ok-probability 0 :fails {:b1 44 :b2 44 :pr1-test 44 :pr2-test 44 :pr3-test 44}}
      ;;        (execute-test model (fn [_] '(false false)))))
      )))

