(defmigration add-tag-table
  (up [] (create
          (tbl :tag
               (varchar :title 25)
               (integer :id :auto-inc :primary-key))))
  (down [] (drop (table :tag))))
