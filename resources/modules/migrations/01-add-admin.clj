(let [table (kc/create-entity "user")]

  (comment Assuming resources/schema/user.clj
           (table :user
                  (integer :id :primary-key :auto-inc)
                  (varchar :name 20)
                  (varchar :password 128)
                  (varchar :salt 128)
                  (boolean :admin)
                  (unique [:name])))

  (defmigration create-chp-admin
    (up []
        (let [{:keys [salt
                      password]} (chp.password/salt-set "admin")]
          (kc/insert table
                     (kc/values
                      {:name "admin"
                       :password password
                       :salt salt
                       :admin true}))))
    (down []
          (kc/delete table
                     (kc/where {:name "admin"})))))
