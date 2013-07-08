(create *db*
 (table :testing
        (integer :id :primary-key :auto-inc)
        (varchar :name 20)
        (unique [:name])))

(create *db*
 (table :testing2
        (integer :id :primary-key :auto-inc)
        (varchar :name 20)
        (unique [:name])))

(create *db*
 (table :testing3
        (integer :id :primary-key :auto-inc)
        (varchar :name 20)
        (unique [:name])))
