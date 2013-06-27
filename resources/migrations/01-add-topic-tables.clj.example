(defmigration add-topic-table
  (up [] (create
          (tbl :topic
                 (varchar :title 50 :unique)
                 (text :content))))
  (down [] (drop (table :topic))))

(defmigration add-topic-subject-table
  (up [] (create
          (tbl :topicSubject
                 (varchar :title 50 :unique)
                 (integer :id :auto-inc :primary-key))))
  (down [] (drop (table :topicSubject))))
