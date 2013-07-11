(ns chp.api
  (:use [chp.db
         :only [*db*]])
  (:require [korma.db
             :as kdb]
            [korma.core
             :as kc]))

(kdb/defdb korma-db *db*)

(def api-dir "resources/api/")

(defn load-settings [table-kw]
  (->> table-kw
      name
      (format "%s%s.clj" api-dir)
      slurp
      load-string))

(defn- api->return 
  ([table-kw] (api->return table-kw []))
  ([table-kw where-map]
     (let [data (load-settings table-kw)
           select (:return data)
           table {:table (name table-kw)}]
       (eval 
        (if-not (seq where-map)
          `(kc/select ~table (kc/fields ~@select))
          `(kc/select ~table
                      (kc/fields ~@select)
                      (kc/where ~where-map)))))))

(defn api->where
  [table-kw where-map]
  (let [data (load-settings table-kw)
        where (:where data)
        where-keys (keys where)
        found (filter #(% where-map) where-keys)
        where (apply merge 
                     (map #(let [f (val %)
                                 k (key %)]
                             (if (k where-map)
                               {k (f (k where-map))}))
                          where))]
    (if-not (seq where)
      (api->return table-kw)
      (api->return table-kw where))))
