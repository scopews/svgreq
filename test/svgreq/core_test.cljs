(ns svgreq.core-test
  (:require
   ["react" :as react]
   ["react-dom/server" :as server]
   [svgreq.core :as svgreq]
   [cljs.test :as t :refer [deftest is]]))

(deftest embed-test
  (is (= (.renderToString
          server
          (react/createElement
           (svgreq/embed "test/svgreq/fixtures" "bell.svg" nil)))
         "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"none\" viewBox=\"0 0 16 16\" height=\"16\" width=\"16\"><path fill=\"white\" d=\"M14.336 10.8863C13.4389 10.4716 12.9076 9.35068 12.7778 7.27702C12.6243 4.85589 12.0813 2.64772 9.15375 2.19936C9.08292 1.35869 8.5281 1 7.97328 1C7.41846 1 6.88725 1.38111 6.81642 2.19936C3.90066 2.64772 3.35765 4.83347 3.19238 7.27702C3.06253 9.21617 2.59034 10.4604 1.63416 10.8975C1.28002 11.0657 1 11.1924 1 11.6373C1 12.0822 1.26822 12.209 1.87026 12.209L14.1117 12.209C14.7138 12.209 15 12.0993 15 11.6373C15 11.1753 14.7256 11.0657 14.336 10.8863ZM7.97328 15C8.72402 15 9.48282 14.6559 9.72423 13.8793C9.77511 13.7157 9.72423 13.3299 9.39166 13.3299C9.05908 13.3299 6.94932 13.3299 6.61669 13.3299C6.28405 13.3299 6.1849 13.6405 6.21803 13.7778C6.41734 14.6044 7.17361 15 7.97328 15Z\" clip-rule=\"evenodd\" fill-rule=\"evenodd\"></path></svg>"
        )
      "Render SVG"))
