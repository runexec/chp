(ns chp.migration
  (:refer-clojure
   :exclude [complement alter drop
             bigint boolean char double float time])
  (:use [chp.db 
         :only [*db*]]
        (lobos core
               connectivity
               migration)))

(def migration-dir "resources/migrations/")

(defn- chp-migrations 
  "Returns sorted list of migration paths"
  []
  (->> migration-dir
       clojure.java.io/file
       file-seq
       (map (memfn getName))
       (filterv #(.. % (endsWith ".clj")))
       (mapv #(str migration-dir %))
       sort))

(defn load-migrations []
    (->> (chp-migrations)
         (map slurp)
         (apply str)
         (format "(do %s)")
         read-string))

(defn chp-migrate 
  "Only to be used with lein migrate alias"
  []
  (try (open-global *db*)
       (catch Exception e (-> e .getMessage println)))
  (migrate))


(defn chp-rollback 
  "Only to be used with lein rollback alias"
  [] 
  (try (open-global *db*)
       (catch Exception e (-> e .getMessage println)))
  (rollback))
