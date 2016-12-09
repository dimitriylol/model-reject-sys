(ns model-reject-sys.core
  (:require [model-reject-sys.state-vector.generate :refer [execute-tests]]
            [model-reject-sys.model :refer [init-model]]))

(defn -main [] (execute-tests (init-model)))
