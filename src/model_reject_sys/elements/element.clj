(ns model-reject-sys.elements.element)
(defrecord Element [])

(defn ->Element [values-map]
  (let [default-values {:phys-state true :logic-state true}]
    (Element. nil (merge default-values values-map))))

(defn bool-to-int [bool] (if bool 1 0))

(defn el-probability [{state :phys-state rej-prob :rejection-prob}]
  (if state
    (- 1 rej-prob)
    rej-prob))
