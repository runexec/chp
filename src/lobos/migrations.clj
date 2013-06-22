(ns lobos.migrations
  (:refer-clojure
   :exclude [alter drop
             bigint boolean char double float time])
  (:use (lobos [migration :only [defmigration]]
               core 
               schema
               helpers
               connectivity)
        [chp.db 
         :only [*db*]]
        [chp.migration 
         :only [load-migrations]]))

(eval (load-migrations))


