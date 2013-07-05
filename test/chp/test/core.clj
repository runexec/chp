(ns chp.test.core
  (:require [clojure.test :refer :all]
            [chp.core :refer :all]))

(deftest correct-paths?
  (testing "Correct core paths"
    (let [dummy-file "file.chtml"
          chp-path-test (str root-path dummy-file)
          chp-template-test (str template-path dummy-file)]
      (are [x y] (= x y)
           (chp-path dummy-file) chp-path-test
           (chp-template-path dummy-file) chp-template-test))))

(deftest global-bindings?
  (testing "Global $ macros"
    (let [page {:params {:abc 123}
                :method :get
                :uri "/url"
                :ip "0.0.0.0"
                :headers "GET / etc..."
                :user-agent "FF23"}]
      (binding [*page* page]
        (are [x y] (= x y)
             ($ params) {:abc 123}
             ($ method) :get
             ($ uri) "/url"
             ($ ip) "0.0.0.0"
             ($ headers) "GET / etc..."
             ($ user-agent) "FF23"
             ($ server-ip) (chp.server/addr)
             ($ server-name) (chp.server/host-name))))))

(deftest global-headers?
  (testing "Global $$ macro"
    (let [agent "FF23"
          page {:headers {"user-agent" agent}}]
      (binding [*page* page]
        (is (= ($$ user-agent) agent))))))

(deftest global-params?
  (testing "Global $p macro"
    (let [param 123
          page {:params {:abc param}}]
      (binding [*page* page]
        (is (= ($p abc) param))))))

(deftest global-env?
    (testing "Global env macro"
      (let [-var (get (System/getProperties) "java.vm.name")]
        (is (= (env java.vm.name) -var)))))

(deftest chp-route?
  (testing "Returning chp-route fn"
    (is (= true (fn? (chp-route "/123" "123"))))))

(deftest chp-when-bound?
  (testing "chp-when return value when getting proper method?"
    (let [-when 123
          page {:method :get}]
      (binding [*page* page]
        (is (= (chp-when :get -when) -when))))))

(deftest chp-body-methods?
  (let [-get :g
        -post :p
        -head :h
        -put :p
        -delete :d
        -not-found :not-found]
    (testing "chp-body GET method body"
      (binding [*page* {:method :get}]
        (is (= (chp-body {:-get -get}) -get) -get)))
    (testing "chp-body POST method body"
      (binding [*page* {:method :post}]
        (is (= (chp-body {:-post -post}) -post))))
    (testing "chp-body HEAD method body"
      (binding [*page* {:method :head}]
        (is (= (chp-body {:-head -head}) -head))))
    (testing "chp-body PUT method body"
      (binding [*page* {:method :put}]
        (is (= (chp-body {:-put -put})) -put)))
    (testing "chp-body DELETE method body"
      (binding [*page* {:method :delete}]
        (is (= (chp-body {:-delete -delete}) -delete))))
    (testing "chp-body NOT-FOUND method body"
      (binding [*page* {:method :abc}]
        (is (= (chp-body {:-not-found -not-found}) -not-found))))))

(deftest chp-file-parsing?
  (testing "parsing chp file"
    (let [chtml "<clj>(print (str 123))</clj>"
          tmp-file (java.io.File/createTempFile "unit-test.chp" ".tmp")
          tmp-file-name "unit.test.chtml"
          tmp-file2 (clojure.java.io/file (chp-path tmp-file-name))
          _ (.. tmp-file deleteOnExit)
          _ (.. tmp-file2 createNewFile)
          _ (.. tmp-file2 deleteOnExit)
          path (.. tmp-file getAbsolutePath)
          path2 (.. tmp-file2 getAbsolutePath)
          write! (spit path chtml)
          write! (spit path2 chtml)]
      (are [x] (= x "123")
           (chp-parse path)
           (root-parse tmp-file-name)))))

(deftest chp-dir-chtml-seq?
  (testing "chp-dir returns all chtmls in dir"
    (let [fp (doto (java.io.File/createTempFile "tmp" ".chtml")
               .deleteOnExit)
          fp-name (.. fp getName)
          path (.. fp getParent)]
      (is (seq
           (filter #(= (.. % getName) fp-name)
                   (file-seq (clojure.java.io/file path))))))))

(deftest chtmls-fn-listing-root-dir?
  (testing "chtmls fn = (chp-dir root-path)"
    (is (= (chtmls) (chp-dir root-path)))))


(defchp chproute-test :data)

(deftest defchp-working?
  (testing "defchp binding test"
    (is (= chproute-test (chp-routes :data)))))
