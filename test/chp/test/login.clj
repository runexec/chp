(ns chp.test.login
  (:require [clojure.test :refer :all]
            [chp.login :refer :all]))

;; This test is assuming the database migration has been applied
;; from the file resources/migrations/01-add-admin.clj

(deftest login-working?
  (testing "Login working?"
    (is (true? (login? "admin" "admin")))))

(deftest admin-working?
  (testing "Admin working?"
    (is (true? (admin? "admin")))))

(deftest login-id-working?
  (testing "Login id?"
    (is (integer? (login-id "admin")))))

(deftest login-salt-available?
  (testing "Salt exist?"
    (is (= 128
           (count
            (login-salt "admin"))))))

(deftest login-salted-password-available?
  (testing "Salted password exist?"
    (is (= 128
           (count
            (login-password "admin"))))))

