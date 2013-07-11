
;; CHP Admin module
;; {:module-type [:module :module :module etc..]}

{:schema [:user] ;; resources/modules/schema/user.clj
 :migrations [:01-add-admin] ;; resources/modules/migrations/01-add-admin.clj
 :bindings [:user] ;; resources/modules/bindings/user.clj
 :middleware [] ;; resources/modules/middleware/ -- nothing
 :cljs [] ;; resources/modules/cljs/ -- nothing
 :api []} ;; resources/modules/api/ -- nothing
