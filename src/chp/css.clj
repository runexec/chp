(ns chp.css
  (:require garden.core))

(defn css [css-coll]
  (println
   (garden.core/css css-coll)))
