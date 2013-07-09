(ns lobos.migrations
  (:refer-clojure
   :exclude [alter drop
             bigint boolean char double float time])
  (:require [korma.db
             :as kdb]
            [korma.core
             :as kc])
  (:use (lobos [migration :only [defmigration]]
               core 
               schema
               helpers
               connectivity)
        [chp.core
         :exclude [korma-db]]
        [chp.login
         :exclude [korma-db]]
        chp.password
        [chp.db 
         :only [*db*]]
        [chp.migration 
         :only [load-migrations]]))

(kdb/defdb korma-db *db*)

(eval (load-migrations))
