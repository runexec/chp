(ns chp.core
  (:use compojure.core
        [noir.session
         :only [wrap-noir-flash
                wrap-noir-session]]
        [chp.db
         :only [*db*]])
  (:require chp.server
            [compojure.handler :as handler]
            [clojure.string
             :as string]
            [korma.db
             :as kdb]
            [korma.core
             :as kc]))


(kdb/defdb korma-db *db*)

(def root-path "chp-root/")
(def template-path (str root-path "templates/"))
(def middleware-path "resources/middleware/")

(defn chp-path [fp] (str root-path fp))
(defn chp-template-path [fp] (str template-path fp))

(defn load-middleware
  ([] (load-middleware middleware-path))
  ([path]
     (let [fp-seq (-> middleware-path
                      clojure.java.io/file
                      file-seq)
           cljs (for [f fp-seq
                      :let [fp (.. f getAbsolutePath)]
                      :when (.. fp (endsWith ".clj"))]
                  (format "(do (ns chp.core) %s )" (slurp fp)))]
         (map #(-> % meta :name eval)
              (map load-string cljs)))))

(def ^:dynamic *title* "CHP Page")

(def ^:dynamic *page*
  {:method nil
   :params nil
   :uri nil})

(def ^:dynamic *cljdb*)

(defn global-params [] (:params *page*))
(defn global-method [] (:method *page*))
(defn global-uri [] (:uri *page*))
(defn global-ip [] (:ip *page*))
(defn global-headers [] (:headers *page*))
(defn global-user-agent [] (:user-agent *page*))
(defn global-server-ip [] (chp.server/addr))
(defn global-server-name [] (chp.server/host-name))

(defmacro $ 
  "To call (global-fn-name). ($ uri) is the  same as (global-uri)"
  [global-symbol]
  `(~(->> global-symbol
          (str "global-")
          symbol)))

(defmacro $$
  "Pull value straight from header"
  [attribute-name]
  `(get (global-headers) ~(str attribute-name)))

(defmacro $p
  [param-name]
  `(get (global-params) ~(keyword param-name)))


(defmacro $cljdb
  [table-keyword where-amap & body]
  `(binding [*cljdb* (first
                      (kc/select {:table (name ~table-keyword)}
                                 (kc/where ~where-amap)))]
     ~@body))
     

(defmacro $db [attribute-name]
  (let [an (keyword attribute-name)]
    `(~an *cljdb*)))

(defmacro env 
  "Returns an environment variable value"
  [key]
  `(System/getProperty (name '~key)))



(defmacro chp-route
  [path & body]
  `(ANY ~path {-headers# :headers
               -ip# :remote-addr
               -params# :params
               -method# :request-method
               -uri# :uri}
        (binding [*page* (assoc *page*
                           :headers -headers#
                           :user-agent (get -headers# "user-agent")
                           :ip -ip#
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
        placements (distinct (re-seq #"(?is)<clj>.*?</clj>" _))
        get-clj #(-> (str %)
                     (string/split #"<clj>")
                     last
                     (string/split #"</clj>")
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

(defn root-parse
  "prepends the root-path var to the provided path and
   call chp-parse on the modified path"
  [path] (-> path chp-path chp-parse))

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

(defn chp-routes [& -routes] -routes)

(defmacro defchp [-symbol & chp-routes]
  `(def ~-symbol (chp-routes ~@chp-routes)))

(defn chp-routing [& -chp-routes]
  ;;; (-> (apply routes ...) middleware-wrap xyz-wrap)
  (let [auto-middleware (fn [x] 
                             (let [wrapped (atom x)]
                               (doseq [m (load-middleware)]
                                 (swap! wrapped m))
                               @wrapped))]
    (-> (apply routes
               (reduce into [] -chp-routes))
        wrap-noir-flash
        wrap-noir-session
        auto-middleware)))

(defn chp-site [& defchp-routes]
  (handler/site
   (apply chp-routing defchp-routes)))
