(ns model-reject-sys.model-test
  (:require [model-reject-sys.model :refer :all]
            [model-reject-sys.elements.adapters :refer [->Adapter]]
            [model-reject-sys.elements.processors :refer [->Processor]]
            [model-reject-sys.elements.redistribute :refer [processors-redistribution-table]]
            [model-reject-sys.elements.loadable :refer [incr-load]]
            [clojure.test :refer :all]))

(deftest split-by-size-test
  (testing
      (is (= '((1 2) (3 4 5) (6)) (split-by-size '(1 2 3 4 5 6) '(2 3 1))))
    (is (= '((1) (2)) (split-by-size '(1 2 3 4 5) '(1 1))))
    (is (= '((1 2)) (split-by-size '(1 2) '(2 3 4))))))

(defn disable-all [element]
  (assoc element :phys-state false :logic-state false))

(deftest physical-state-apply-test
  (let [adapters (list (->Adapter :a1-test) (->Adapter :a2-test))
        processors (list (->Processor :pr-test 10 100))
        tested-model (->Model nil nil nil processors nil adapters)
        tested-state-vector '(false false true)]
    (testing
        (is (= (list '() '() '()
                     (list (disable-all (first processors)))
                     '()
                     (cons (disable-all (first adapters)) (rest adapters)))
               (physical-state-apply tested-state-vector tested-model))))))

;; TODO: cover all cases
(deftest redistribute-group-test
  (with-redefs [processors-redistribution-table {:pr1-test '((nil 200))
                                                 :pr2-test '((10 nil))}]
    (let [pr1 (->Processor :pr1-test 10 100)
          pr2 (->Processor :pr2-test 20 100)]
      (testing
          (is (= (list (incr-load pr1 10) (assoc pr2 :phys-state false))
                 (redistribute-group 0 (list pr1 (disable-all pr2)))))))))

;; TODO: cover all cases
(deftest logical-state-apply-test
  (with-redefs [processors-redistribution-table {:pr1-test '((nil 10))
                                                 :pr2-test '((10 nil))}]
    (let [grouped-elems (list (list (disable-all (->Processor :pr1-test 10 30))
                                    (disable-all (->Processor :pr2-test 10 30)))
                              (list (->Adapter :a1-test)
                                    (disable-all (->Adapter :a2-test))))]
      (is (= {:pr1-test false :pr2-test false :a1-test true :a2-test false}
             (logical-state-apply grouped-elems))))))
