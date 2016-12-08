(ns model-reject-sys.loadable-test
  (:require [clojure.test :refer :all]
            [model-reject-sys.elements.processors :refer [->Processor]]
            [model-reject-sys.elements.loadable :refer [incr-load]]))

(deftest incr-load-test
  (let [proc (->Processor :test 10 100)]
    (testing
        (is (= (incr-load proc nil) proc))
      (is (= (incr-load proc 20) (->Processor :test 30 100))))))
