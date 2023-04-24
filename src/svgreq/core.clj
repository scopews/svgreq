(ns svgreq.core
  (:require
   [clojure.walk :as walk]
   [clojure.string :as string]
   [clojure.xml :as xml]))

(defonce db (atom {}))

(defn- kebab-case->camelCase [k]
  (let [words (clojure.string/split (name k) #"-")]
    (->> (map clojure.string/capitalize (rest words))
         (apply str (first words))
         keyword)))

(defn color-value [k v]
  (if (and (#{:fill :stroke} k)
           (= v "#fff"))
    "currentColor"
    v))

(defn- map-keys [m]
  (->> (map (fn [[k v]] [(kebab-case->camelCase k)
                         (color-value k v)]) m)
       (into {})))

(defn- transform-keys [form]
  (walk/postwalk (fn [x] (if (map? x) (map-keys x) x)) form))

(defmacro embed
  [root file {:keys [display-name]}]
  (let [svg-name     (if (string/ends-with? file ".svg")
                       (subs file 0 (- (count file) 4))
                       file)
        svg-fn       (gensym "svg-fn")
        content      (slurp (str root "/" svg-name ".svg"))
        svg-map-sym  (gensym "svg-map-sym")
        svg-map      (xml/parse
                      (java.io.ByteArrayInputStream.
                       (.getBytes content)))
        svg-map      (transform-keys svg-map)
        display-name (if display-name
                       (str display-name)
                       (str (string/capitalize svg-name) "Svg"))
        svg-key      (str root "/" file)]
    (if (contains? @svgreq.core/db svg-key)
      `(let [~svg-map-sym (if ~(with-meta 'goog/DEBUG {:tag 'boolean})
                            ~svg-map
                            (get @svgreq.core/svg-db ~svg-key))
             ~svg-fn      (when ~svg-map-sym
                            (.forwardRef
                             svgreq.core/r
                             #(svgreq.core/create-element
                               ~svg-map-sym
                               (svgreq.util/map->camel-map %1) %2)))]
         (when ~(with-meta 'goog/DEBUG {:tag 'boolean})
           (when ~svg-map-sym
             (set! (.-displayName ~svg-fn) ~display-name)))
         ~svg-fn)
      (do
        (swap! svgreq.core/db assoc svg-key svg-map)
        `(do
           (when-let [~svg-map-sym ~svg-map]
             (swap! svgreq.core/svg-db assoc ~svg-key ~svg-map-sym))
           (let [~svg-fn (.forwardRef
                          svgreq.core/r
                          #(svgreq.core/create-element
                            (get @svgreq.core/svg-db ~svg-key)
                            (svgreq.util/map->camel-map %1) %2))]
             (when ~(with-meta 'goog/DEBUG {:tag 'boolean})
               (set! (.-displayName ~svg-fn) ~display-name))
             ~svg-fn))))))
