(ns model-reject-sys.state-vector.generator
  (:require [clojure.math.combinatorics :as combo]
            [clojure.set :refer [difference]]))

(defn init-state-vector [multiplicity n]
  "Initializing state vector.
  multiplicity is amount of errors, n is size of vector"
  (vec (concat (repeat multiplicity false)
               (repeat (- n multiplicity) true))))

(defn unique-rand-element [n result-set]
  {:pre [(not-empty (difference (into #{} (take n (range))) result-set))]}
  "The same as rand-int, but check for uniqueness in result-set.
  Throw assertion if there isn't any random integer between 0 (inclusive) and n (exclusive)"
  (let [random (rand-int n)]
    (if (contains? result-set random)
      (unique-rand-element n result-set)
      random)))

(defn n-permutations
  "Generates n permutations of input sequence.
  Permutations gathered randomly from all possible permutations.
  If n isn't passed, then all permutations are returned"
  ([input-seq] (combo/permutations input-seq))
  ([input-seq n]
   (n-permutations input-seq n #{}))
  ([input-seq n result-index-set]
   (if (<= n 0)
     '()
     (let [unique-index (unique-rand-element (combo/count-permutations input-seq)
                                             result-index-set)]
       (cons (combo/nth-permutation input-seq
                                    unique-index)
             (n-permutations input-seq (dec n) (conj result-index-set unique-index)))))))


(defn percentile [all percent]
  (int (/ (* all percent) 100)))

(defn percentile-elem [state-vector percent]
  (percentile (combo/count-permutations state-vector) percent))

(defn n-state-vectors
  "Generates n random, uniuque state vectors.
  If n isn't passed, all possible state vectors are returned"
  ([multiplicity size]
   (n-permutations (init-state-vector multiplicity size)))
  ([multiplicity size n]
   (let [state-vector (init-state-vector multiplicity size)]
     (n-permutations state-vector (percentile-elem state-vector n)))))
