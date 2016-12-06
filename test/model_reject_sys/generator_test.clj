(ns model-reject-sys.generator-test
  (:require [clojure.test :refer :all]
            [model-reject-sys.state-vector.generator :refer :all]
            [clojure.set :refer [subset?]]))

(deftest init-state-vector-test
  (testing (is (= '(false true true) (init-state-vector 1 3)))))

(deftest unique-rand-element-test
  (testing
      (try
        (unique-rand-element 2 #{0 1})
        (catch AssertionError a-e (is true "Caught correct assertion")))
    (is (= 2 (unique-rand-element 3 #{0 1})))))

(deftest n-permutations-test
  (let [inp-seq '[0 1 2]
        correct-permutations '([0 1 2] [0 2 1] [1 0 2] [1 2 0] [2 0 1] [2 1 0])
        two-permutations (n-permutations inp-seq 2)]
    (testing
        (is (= (n-permutations inp-seq) correct-permutations))
      (is (= (subset? (into #{} two-permutations) (into #{} correct-permutations))))
      (is (= (count two-permutations) 2))
      (is (empty? (n-permutations inp-seq 0)))
      (is (empty? (n-permutations inp-seq -4))))))
