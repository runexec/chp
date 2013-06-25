(ns chp.routes.{{namespace}}
  (:use chp.core))

;; :table gets passed to functions in chp.builder, and these functions
;; turn the :table->:keyword to resources/bindings/keyword.clj. Removing the 
;; regex restraints can be potentially dangerous.

(defchp {{namespace}}-table-routes
  (chp-route "/list/{{name}}" 
             (root-parse "{{name}}/list.chtml"))
  (chp-route "/new/{{name}}"             
               (root-parse "{{name}}/new.chtml"))
  (chp-route ["/edit/{{name}}/:id"
              :id #"\d+"]
             (root-parse "{{name}}/edit.chtml"))
  (chp-route ["/view/{{name}}/:id"
              :id #"\d+"]
               (root-parse "{{name}}/view.chtml")))
