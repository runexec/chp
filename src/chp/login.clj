(ns chp.login
  (:require [korma.db
             :as kdb]
            [korma.core
             :as kc])
  (:use [chp.db
         :only [*db*]]
        [chp.password
         :only [password]]))
  
(kdb/defdb korma-db *db*)


(defn login-id 
  [user
   & {:keys [username-column]
      :or {username-column :name}}]
  (let [column (keyword username-column)
        -user {column (str user)}]
    (->> (kc/where -user)
         (kc/select {:table "user"} (kc/fields [:id]))
         first
         :id)))

(defn login?
  [user
   secret
   & {:keys
      [username-column
       password-column]
      :or {username-column :name
           password-column :password}}]
  (let [[uc pc] [(keyword username-column)
                 (keyword password-column)]
        user {uc user}
        table {:table "user"}
        salt (->> (kc/where user)
                  (kc/select table (kc/fields [:salt]))
                  first
                  :salt)
        login (assoc user
                :password
                (password salt secret))]
    (->> (kc/where login)
         (kc/select table (kc/fields [:id]))
         first
         boolean)))

(defn admin?
  [user 
   & {:keys
      [username-column
       admin-column]
      :or {username-column :name
           admin-column :admin}}]
  (let [uc (keyword username-column)
        ac (keyword admin-column)
        admin {uc user
               ac true}]
    (->> (kc/where admin)
         (kc/select {:table "user"})
         first
         boolean)))
