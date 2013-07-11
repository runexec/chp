
;; API settings for the User table
;; the filename must be the same as the table name
;; {:table :user} => user.clj

{:table :user
 :return [:id :name]

 ;; The where key holds a map that describes 
 ;; columns that can be used to locate the 
 ;; data.

 ;; Each column key needs to have a function
 ;; as the value that accepts one arg. This
 ;; function needs to convert the arg to the
 ;; proper data type.

 ;; The single arg is a string from the uri

 :where {:id #(Integer. %)
         :name str
         :admin #(Boolean. %)}}
