(ns chp.routes.user
  (:use chp.core))

;; :table gets passed to functions in chp.builder, and these functions
;; turn the :table->:keyword to resources/bindings/keyword.clj. Removing the 
;; regex restraints can be potentially dangerous.

(defchp user-table-routes
  (chp-route "/list/user" 
             (root-parse "user/list.chtml"))
  (chp-route "/new/user"             
               (root-parse "user/new.chtml"))
  (chp-route ["/edit/user/:id"
              :id #"\d+"]
             (root-parse "user/edit.chtml"))
  (chp-route ["/view/user/:id"
              :id #"\d+"]
               (root-parse "user/view.chtml")))
