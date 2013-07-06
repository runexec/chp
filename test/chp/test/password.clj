(ns chp.test.password
  (:require [clojure.test :refer :all]
            [chp.password :refer :all]))


(deftest password-with-salt?
  (is (= (password 1 "2")
         (str "5aadb45520dcd8726b2822a7a78bb53d794f"
              "557199d5d4abdedd2c55a4bd6ca73607605c"
              "558de3db80c8e86c3196484566163ed1327e"
              "82e8b6757d1932113cb8"))))

(deftest gen-salt?
  (is (= 128 (count (salt)))))

(deftest password-salt-set?
  (let [_ (salt-set "admin")
        salt (:salt _)
        secret (:password _)
        clear-text (:clear-text _)]
    (is (= 128 (count salt)))
    (is (= 128 (count secret)))
    (is (= secret (password salt clear-text)))))

