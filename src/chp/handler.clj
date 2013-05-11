(ns chp.handler
  (:use compojure.core
        chp.html
        chp.css)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.string :as string]
            [korma.db :as kdb]
            [korma.core :as kc]))

(def root-path "chp-root/")

(defn chp-path [fp] (str root-path fp))

(def ^:dynamic *title* "CHP Page")
(def ^:dynamic *page*
  {:method nil
   :params nil
   :uri nil})

(defn global-params [] (:params *page*))
(defn global-method [] (:method *page*))
(defn global-uri [] (:uri *page*))

(defmacro $ 
  "To call (global-fn-name). ($ uri) is the  same as (global-uri)"
  [global-symbol]
  `(~(->> global-symbol
          (str "global-")
          symbol)))

(defn env 
  "Returns an environment variable value"
  [-key-str]
  (System/getProperty -key-str))

(defmacro chp-route
  [path & body]
  `(ANY ~(str path) {-params# :params
                     -method# :request-method
                     -uri# :uri}
        (binding [*page* (assoc *page*
                           :method -method#
                           :params -params#
                           :uri -uri#)]
            ~@body)))

(defmacro chp-when
  "kw-method = (:get|:head|:put|:post|:delete)"
  [kw-method & body]
  `(when (= ~'($ method)
          (keyword ~kw-method))
     ~@body))

(defn chp-body
  [{:keys [-not-found
           -get 
           -post 
           -head 
           -put 
           -delete]}]
  (let [_ ($ method)]
    (or (when (= _ :get) -get)
        (when (= _ :post) -post)
        (when (= _ :head) -head)
        (when (= _ :put) -put)
        (when (= _ :delete) -delete)
        -not-found)))

(defn chp-parse 
  "Parses chtml files"
  [path]
  (let [_ (slurp path)
        placements (distinct (re-seq
                              #"(?is)<clojure>.*?</clojure>"
                              _))
        get-clj #(-> (str %)
                     (string/split #"<clojure>")
                     last
                     (string/split #"</clojure>")
                     first)
        values (map #(do {:replace (str %)
                          :return (with-out-str
                                    (->> %
                                         get-clj
                                         (str "(ns chp.handler) ")
                                         load-string))})
                    placements)]
    (loop [body _
           values values]
      (if-not (seq values)
        body
        (let [v (first values)
              r (:replace v)
              value (:return v)]
          (recur
           (.. body 
               (replace r value))
           (rest values)))))))

(defn chp-dir [path]
  (let [chtmls (filter #(not= nil %)
                       (map #(when (.. % 
                                       getName
                                       (endsWith ".chtml"))
                               (.. % getName))
                            (-> path
                                clojure.java.io/file
                                .listFiles)))]
    (map #(str path %) chtmls)))

(defn chtmls [] (chp-dir root-path))    



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Routes

(defroutes app-routes
  (chp-route "/chtml" 
             (binding [*title* "Test Page Example"]
               (or (chp-parse (str root-path "test-page.chtml"))
                   "error")))
  (chp-route "/"
             (let [display (str "Method " ($ method) "<br />"
                                "URI " ($ uri) "<br />"
                                "Params " ($ params) "<br />")]
               (chp-body {:-get (str "Get => " display)
                          :-post (str "Post => " display)
                          :-not-found "Sorry, but this page doesn't exist"})))
  (chp-route "/testing"
             (or (chp-when :get
                           (str "chp-body wasn't used to access "
                                ($ uri)))
                 "Not Found"))
  (chp-route "/chp"
             (or (chp-parse (str root-path "chp-info.chtml"))
                 "error"))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
