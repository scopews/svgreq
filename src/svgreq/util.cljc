(ns svgreq.util
  (:require
   [clojure.string :as string]))

(def ^:private invalid-props
  #{"element-ref"})

(defn ->camel-case
  "Returns camel case version of the string, e.g. \"font-size\"
  becomes \"fontSize\"."
  [value]
  (let [[first-word & words] (string/split value #"-")]
    (if (or (= first-word "data")
            (= first-word "area"))
      value
      (-> (map string/capitalize words)
          (conj first-word)
          string/join))))

(defn map->camel-map
  "Convert keys of map to camel case version."
  [props]
  (.reduce (.keys js/Object props)
           (fn [acc k]
             (when-not (invalid-props k)
               (aset acc (->camel-case k) (aget props k)))
             acc)
           #js {}))
