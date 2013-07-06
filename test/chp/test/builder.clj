(ns chp.test.builder
  (:use [chp.db
         :only [*db*]]
        chp.core
        chp.html
        chp.password
        [chp.login :exclude [korma-db]])
  (:require [clojure.test :refer :all]
            [chp.builder :refer :all]
            [korma.core
             :as kc]
            [korma.db
             :as kdb]
            [noir.session
             :as session]))

;; This test is assuming the default bindings from 
;; the file resources/bindings/user.clj. The migration
;; from file resources/migrations/01-add-admin.clj is
;; also assumed to have been applied.

(def test-binding
  (->> "user.clj"
       (str binding-dir)
       slurp
       read-string
       eval))

(deftest binding-map-test
  (testing "bindings/file.clj => PersistentArrayMap"
    (is (= clojure.lang.PersistentArrayMap
           (class test-binding))))
  (testing "binding has required parent keys"
    (is (= (sort (keys test-binding))
           '(:edit
             :edit-enforce
             :list
             :table
             :view))))
  (testing "binding has required value matches"
    (let [[view edit enforce] ((juxt :view 
                                     :edit 
                                     :edit-enforce)
                               test-binding)]
      (is (and (apply = (map count [view edit enforce]))
               (every? true?
                       (map #(= %1 %2 %3)
                            (sort view)
                            (-> edit keys sort)
                            (-> enforce keys sort))))))))

(deftest path-helpers
  (testing "bound fn returning proper path?"
    (is (= (bound :user)
           (str binding-dir "user.clj")))))

(deftest list-table-fn-test
  (testing "getting 1 user from the user table"
    ;; 0 = start row, 1 = stop row
    (let [users (list-table :user 0 1 :id :name)
          user (first users)]
      (is (= (count users) 1))
      (is (every? true?
                  (map #(= %1 %2)
                       (-> user keys sort)
                       (sort '(:id :name))))))))

(deftest binding-to-data
  (testing "binding->data fn properly loading bindings"
    (is (= (-> test-binding keys sort)
           (-> :user binding->data keys sort)))))

(deftest binding-to-list
  (testing "binding->list fn listing values via :list"
    (is (= 1 (count
              (binding->list :user 0 1))))))

(deftest binding-to-view
  (testing "binding->view fn values via :view"
    (let [user (first (list-table :user 0 1 :id :name))
          id (:id user)
          user-view (binding->view :user id)
          user-keys (keys user-view)]
      (is (= (sort user-keys)
             (-> test-binding :view sort))))))

(deftest binding-to-edit
  (testing "binding->edit fn values via :edit"
    (let [_ (first (list-table :user 0 1 :id :name))
          id (:id _)
          user (binding->view :user id)
          edit-fns (:edit test-binding)]
      (is (= (binding->edit :user id)
             (apply str
                    (for [k (-> edit-fns keys reverse)
                          :let [display-fn (k edit-fns)
                                -value (k user)]]
                      (str (label (str k) (name k))
                           "<br />"
                           (display-fn -value)
                           "<br /><br />"))))))))

(deftest bining-new
  (testing "binding->new fn values via :edit"
    (let [_ (first (list-table :user 0 1 :id :name))
          id (:id _)
          user (binding->view :user id)
          edit-fns (:edit test-binding)]
      (is (= (binding->new :user)
             (apply str
                    (for [k (keys user)
                          :let [display-fn (k edit-fns)
                                -name (name k)
                                -label (label -name -name)
                                -display (display-fn "")]]
                      (format "%s<br />%s<br />"
                              -label
                              -display))))))))

(deftest enforcers 
  (testing "proper amount of enforncers of same key?"
    (let [be (binding->enforce :user)
          enforce-data (-> :user binding->data :edit-enforce)]
      (is (every? fn? (vals enforce-data)))
      (is (= (-> enforce-data keys sort)
             (-> (binding->enforce :user) keys sort)))))) 

(deftest binding-existnce
  (testing "user binding-exist? via binging-exist? fn"
    (is (= true (binding-exist? :user)))))
