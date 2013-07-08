(ns chp.schema
  (:refer-clojure :exclude [bigint
                            boolean
                            char
                            double
                            float
                            time])
  (:use [chp.db
         :only [*db*]]
        [lobos.core
         :only [create]]))

(def schema-dir "resources/schema/")

(defn- schema-files []
  (->> schema-dir
       clojure.java.io/file
       file-seq
       (map (memfn getName))
       (filter #(.. % (endsWith ".clj")))
       (map #(str schema-dir %))))

(defn- fp->schema 
  "File path to schema"
  [schema-src-path]
  (->> schema-src-path
       slurp
       (format "(use '[lobos.core :only [create]] '[chp.db :only [*db*]]) %s")
       (format "(do %s)")
      read-string
      eval))

(defn load-schemas
  "Only supposed to be used in the CLI.
   lein schema"
  []
  (use 'lobos.schema)
  (doseq [_ (schema-files)]
    (println "Creating Table => " _)
    (let [result (try
                   (fp->schema _)
                   true
                   (catch Exception e
                     (println e)
                     false))]
      (if result
        (println "OKAY")
        (println "FAIL")))))
