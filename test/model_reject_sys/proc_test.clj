(ns model-reject-sys.proc-test
  (:require  [clojure.test :refer :all]
             [model-reject-sys.processors :refer :all]))

(deftest redistributable-on?-test
  (let [test-proc (->Processor :test 40 100)]
    (testing
        (is (= (redistributable-on? test-proc 60) true))
      (is (= (redistributable-on? test-proc 100) false))
      (is (= (redistributable-on? test-proc nil) true)))))

(deftest apply-redistributable-rule-test
  (let [redistributable-rule '(nil 10 nil 10)
        fail-redistributable-rule '(nil 100 nil nil)
        processors (list (->Processor :test1 10 100)
                         (->Processor :test2 20 100)
                         (->Processor :test3 30 100)
                         (->Processor :test4 40 100))]
    (testing
        (is (= (apply-redistributable-rule redistributable-rule processors) true))
      (is (= (apply-redistributable-rule fail-redistributable-rule processors) false)))))

(deftest incr-load-test
  (let [proc (->Processor :test 10 100)]
    (testing
        (is (= (incr-load proc nil) proc))
      (is (= (incr-load proc 20) (->Processor :test 30 100))))))

(deftest redistribute-test
  (with-redefs [processors-redistribution-table {:test1 '((nil 10 nil 10) (nil nil 20 nil))}]
    (let [processors (list (->Processor :test1 10 100)
                           (->Processor :test2 20 100)
                           (->Processor :test3 30 100)
                           (->Processor :test4 40 100))
          redistributed-processors (list (->Processor :test1 10 100)
                                         (->Processor :test2 30 100)
                                         (->Processor :test3 30 100)
                                         (->Processor :test4 50 100))]
      (testing
          (is (= (redistribute (first processors) processors)
                 redistributed-processors))))))
