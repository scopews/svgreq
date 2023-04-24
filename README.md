# Svgreq
Clojure macro for generating react component based on svg

## Usage
```clojure
(def BellSvg (svgreq.core/embed "resources/icons" "bell"))
```

To simplify the definition of an SVG component, you can create a simple macro:
```clojure
(defmacro defsvgc
  [sym icon]
  `(def ~sym (svgreq.core/embed
              "resources/icons" ~icon
              {:display-name ~sym})))
```
and usage
```clojure
(defsvgc BellSvg "bell")
```
