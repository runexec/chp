(ns chp.builder
  (:use [chp.db
         :only [*db*]]
        chp.core
        chp.html
        chp.password
        [chp.login :exclude [korma-db]])
  (:require [korma.core
             :as kc]
            [korma.db
             :as kdb]
            [noir.session
             :as session]))

(kdb/defdb korma-db *db*)

(def binding-dir "resources/bindings/")

(defn bindings []
  (->> binding-dir
       clojure.java.io/file
       file-seq
       (map (memfn getName))
       (filter #(.. % (endsWith ".clj")))
       (map #(str binding-dir %))
       (map #(-> % slurp read-string eval))))

(defn bound
  "name is a keyword. name becomes (sr binding-dir name \".clj\")
   chp.builder> (binding :user)
   \"resources/bindings/user.clj\""
  [-name]
  (str binding-dir
       (name -name)
       ".clj"))

(defn list-table 
  "table is a keyword. offset and limit are ints.
   fields are keywords of column names."
  [table offset limit & fields]
  (let [field (apply list kc/fields fields)]
    (eval 
     `(do (kc/select (keyword ~table)
                     ~field
                     (kc/offset (or ~offset 0))
                     (kc/limit (or ~limit 20)))))))

(defn binding->data 
  "-name = (= :user \"resources/bindings/user.clj\")"
  [-name] 
  (-> -name bound slurp read-string eval))

(defn binding->list 
  "-name = (= :user \"resources/bindings/user.clj\")"
  [-name offset limit]
  (let [data (binding->data -name)
        table (:table data)
        fields (:list data)]
    (apply list-table table offset limit fields)))

(defn binding->view
  "-name = (= :user \"resources/bindings/user.clj\")"
  [-name id]
  (let [data (binding->data -name)
        table (:table data)
        bound-keys (:view data)
        fields (apply
                (partial kc/fields
                         (kc/select* table))
                bound-keys)]
    (-> fields
        (kc/where {:id id})
        kc/select
        first)))

(defn binding->edit 
  "-name = (= :user \"resources/bindings/user.clj\")"
  [-name id]
  (let [data (binding->data -name)
        table (:table data)
        value-set (:edit data)
        bound-keys (keys value-set)
        fields (apply
                (partial kc/fields
                         (kc/select* table))
                bound-keys)]
    (apply str
           (for [column (-> fields
                            (kc/where {:id id})
                            kc/select
                            first)
                 :let [k (key column)
                       display-fn (k value-set)
                       -value (val column)]]
             (str (label (str k) (name k))
                  "<br />"
                  (display-fn -value)
                  "<br /><br />")))))

(defn binding->new 
    "-name = (= :user \"resources/bindings/user.clj\")"
    [-name]
    (let [data (-> -name binding->data :edit)]
      (apply str
             (for [_ (reverse data)
                   :let [-name (-> _ key name)
                         -label (label -name -name)
                         -display ((-> _ val) "")]]
                   (format "%s<br />%s<br />"
                           -label
                           -display)))))
              
(defn binding->enforce
    "-name = (= :user \"resources/bindings/user.clj\")"
    [-name]
    (-> -name binding->data :edit-enforce))


(defn binding-exist? [table-name]
  (let [table {:table
               (if (keyword? table-name)
                 (name table-name)
                 (str table-name))}]
    (try (first (kc/select table))
         true
         (catch Exception ex false))))
