(table :user
       (integer :id :primary-key :auto-inc)
       (varchar :name 20)
       (varchar :password 128)
       (varchar :salt 128)
       (boolean :admin)
       (unique [:name]))
