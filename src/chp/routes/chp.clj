(ns chp.routes.chp
  (:use chp.core
        [cheshire.core
         :only [generate-string]]
        [chp.api
         :only [api->where]]
        [chp.builder
         :only [binding-exist?]]))

(defn display? [] (binding-exist? ($p table)))

(defchp chp-builder-paths
  (chp-route "/chp/api" (root-parse "chp/api.chtml"))
  (chp-route  ["/chp/api/:table" :table  #"[a-zA-Z0-9-_]*"]
             (try
               (let [table ($p table)
                     return (api->where (keyword table) ($ params))]
                 (if-not (seq return)
                   "{}"
                    (let [return (mapv #(generate-string % {:pretty true})
                                       return)
                          rc (count return)]
                      (format "{\"data\": [%s]}"
                              (apply str
                                     (if (<= rc 1)
                                       return
                                       (drop-last
                                        (interleave return 
                                                    (repeat rc ",")))))))))
               (catch Exception e
                 (println e "=>" (.. e getMessage))
                 "An error occured")))
  (chp-route "/chp/login"
             (or (chp-when :get (root-parse "chp/login.chtml"))
                 (chp-when :post (root-parse "chp/login-session.chtml"))
                 "Not Found"))

;; :table gets passed to functions in chp.builder, and these functions
;; turn the :table->:keyword to resources/bindings/keyword.clj. Removing the 
;; regex restraints can be potentially dangerous.

  (chp-route ["/chp/list/:table" :table  #"[a-zA-Z0-9-_]*"]
             (if (display?)
               (root-parse "chp/list.chtml")
               "Not Found"))
  (chp-route ["/chp/new/:table" :table  #"[a-zA-Z0-9-_]*"]
             (if (display?)
               (root-parse "chp/new.chtml")
               "Not Found"))
  (chp-route ["/chp/edit/:table/:id"
              :table  #"[a-zA-Z0-9-_]*"
              :id #"\d+"]
              (if (display?)
                (root-parse "chp/edit.chtml")
                "Not Found"))
  (chp-route ["/chp/view/:table/:id"
              :table  #"[a-zA-Z0-9-_]*"
              :id #"\d+"]
             (if (display?)
               (root-parse "chp/view.chtml")
               "Not Found")))
