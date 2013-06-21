(ns chp.db)

(def config "resources/config/db.clj")

(def ^:dynamic *db*
  (-> config
      slurp
      read-string))
   
