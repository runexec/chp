(ns chp.routes.chp
  (:use chp.core))

;; :table gets passed to functions in chp.builder, and these functions
;; turn the :table->:keyword to resources/bindings/keyword.clj. Removing the 
;; regex restraints can be potentially dangerous.

(defchp chp-builder-paths
  (chp-route ["/chp/list/:table" :table  #"[a-zA-Z0-9-_.]*"]
               (root-parse "chp/list.chtml"))
  (chp-route ["/chp/new/:table" :table  #"[a-zA-Z0-9-_.]*"]
               (root-parse "chp/new.chtml"))
  (chp-route ["/chp/edit/:table/:id"
              :table  #"[a-zA-Z0-9-_.]*"
              :id #"\d+"]
             (root-parse "chp/edit.chtml"))
  (chp-route ["/chp/view/:table/:id"
              :table  #"[a-zA-Z0-9-_.]*"
              :id #"\d+"]
             (root-parse "chp/view.chtml")))
