(let [table (kc/create-entity "user")]
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
