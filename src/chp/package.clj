(ns chp.package
  ;; for schema load
  (:refer-clojure :exclude [bigint
                            boolean
                            char
                            double
                            float
                            time])
  (:use [chp.schema 
         :only [load-schemas]]
        [chp.migration
         :only [chp-migrate]]
        [chp.module
         :only [mod-enable]]))

(def package-dir "resources/packages/")

(defn force-string [-name]
  (if (keyword? -name)
    (name -name)
    (str -name)))

(defn name->clj [-name]
  (let [-name (force-string -name)
        -file (str -name ".clj")]
    (->> -file
         (str package-dir)
         slurp
         read-string)))


(defn load-package [-name]
  (doseq [_ (name->clj -name)
          :let [[-type mods] _]]
    (println "\n Loading for" -type)
    (doseq [_ mods]
      (mod-enable -type _)
      (case -type
        :schema (load-schemas)
        :migrations (chp-migrate)
        :bindings true
        :middleware true
        :cljs true
        :unknown-type))))
      
(defn load-packages [& -name]
  (doseq [_ -name] (load-package _)))
