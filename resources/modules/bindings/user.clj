;; Example bindings for resources/schema/user.clj
;; All values will be retrieved by the id column

;; table must match the filename withut the clj extension
;; user.clj -> user

{:table :user

;; List view value
;; (chp.builder/binding-list :user 0 10)
;; /chp/list/user

 :list (list :name :id)

;; View view values
;; (chp.builder/binding->view :user 1)
;; site.com/chp/view/user/:id

 :view (list :name :password :admin)

;; Edit view values
;; (chp.builder/binding->edit :user 1)
;; site.com/chp/edit/user/:id 
;; site.com/chp/new/user

;; edit is a hash-set with table columns
;; as the key and the chp.html namespace
;; function used to display the value.

 :edit {:name #(text-field :name (escape %))
        :password #(password-field :password (escape %))
        :admin #(check-box :admin (Boolean/valueOf %))}

;; enforce data type with fn to check and
;; or convert before going into database.
;; The function must take one arg.

;; :name is limited to a string of 20 chars
;; :password is limited to 100 chars
;; :admin mut be a boolean value
 :edit-enforce {:name #(->> % str seq (take 20) (apply str))
                :password  #(->> % str seq (take 100) (apply str))
                :admin #(Boolean/valueOf %)}}
             
