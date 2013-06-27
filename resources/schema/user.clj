(table :user
       (integer :id :primary-key :auto-inc)
       (varchar :name 20)
       (varchar :password 100)
       (varchar :salt 100)
       (boolean :admin)
       (unique [:name]))
