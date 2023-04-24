(ns svgreq.core
  (:require-macros [svgreq.core])
  (:require
   ["react" :as react]
   [cljs-bean.core]
   [svgreq.util]))

(defonce svg-db (atom {}))

(def r react)

(defn create-element
  ([svg]
   (svgreq.core/create-element svg nil nil))
  ([svg props]
   (svgreq.core/create-element svg props nil))
  ([{:keys [tag attrs content]} props ref]
   (react/createElement
    (name tag)
    (.assign js/Object
             (cljs-bean.core/->js attrs)
             #js {:ref ref}
             props)
    (map-indexed
     (fn [key item]
       (svgreq.core/create-element item #js {:key key}))
     content))))
