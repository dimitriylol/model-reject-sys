(ns model-reject-sys.elements.element)

(defrecord Element [])

(defn ->Element [values-map]
  (let [default-values {:phys-state true :logic-state true}]
    (Element. nil (merge default-values values-map))))