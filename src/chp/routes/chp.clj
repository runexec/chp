(ns chp.routes.chp
  (:use chp.core
        [chp.builder
         :only [binding-exist?]]))

(defn display? [] (binding-exist? ($p table)))

;; :table gets passed to functions in chp.builder, and these functions
;; turn the :table->:keyword to resources/bindings/keyword.clj. Removing the 
;; regex restraints can be potentially dangerous.

(defchp chp-builder-paths
  (chp-route ["/chp/list/:table" :table  #"[a-zA-Z0-9-_.]*"]
             (if (display?)
               (root-parse "chp/list.chtml")
               "Not Found"))
  (chp-route ["/chp/new/:table" :table  #"[a-zA-Z0-9-_.]*"]
             (if (display?)
               (root-parse "chp/new.chtml")
               "Not Found"))
  (chp-route ["/chp/edit/:table/:id"
              :table  #"[a-zA-Z0-9-_.]*"
              :id #"\d+"]
              (if (display?)
                (root-parse "chp/edit.chtml")
                "Not Found"))
  (chp-route ["/chp/view/:table/:id"
              :table  #"[a-zA-Z0-9-_.]*"
              :id #"\d+"]
             (if (display?)
               (root-parse "chp/view.chtml")
               "Not Found")))
