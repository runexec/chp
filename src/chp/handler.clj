(ns chp.handler
  (:use compojure.core
        chp.html
        chp.template
        [chp.core
         :exclude [korma-db]]
        [chp.api
         :only [api->where]]
        [chp.db
         :only [*db*]]
        [garden.core
         :only [css]])
  (:require chp.server
            [compojure.route
             :as route]
            [korma.db
             :as kdb]
            [korma.core
             :as kc]
            [noir.session
             :as session]

            ;;;;;;;;;;;;;;;;;;; CHP Builder

            [chp.routes.chp
             :refer [chp-builder-paths]]

            ;;;;;;;;;;;;;;;;;;; Routes

            [chp.routes.example
             :refer [example-routes]]
            [chp.routes.user
             :refer [user-table-routes]]))

(kdb/defdb korma-db *db*)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Routes

(defchp app-routes

  ;; Load CHP File

  (chp-route "/chtml" 
             (binding [*title* "Test Page Example"]
               (or (root-parse "test-page.chtml")
                   "error")))
  (chp-route "/chp"
	     ;; root-parse = root-path "/" file
             (or (root-parse "chp-info.chtml")
                 "error"))
  (chp-route "/session"
            (or (root-parse "session-example.chtml")
                "error"))

  ;; Named params

  (chp-route "/index/:id"
             (format "ID is %s" 
                     (escape ($p id))))
  (chp-route "/index/:id/:action"
             (format "Action is %s" 
                     (escape ($p action))))

  ;; Multiple handlers under a single route

  (chp-route "/testing"
             (or 
              (chp-when :post "POST METHOD RETURN")
              (chp-when :get
                        (str (format "chp-body wasn't used to access %s from %s with %s"
                                     ($ uri) ($ ip) ($ user-agent))
                             (format "<p>Tracking you? DNT HTTP Header = %s</p>" ($$ dnt))
                             (format "<p>HTTP Header cache-control = %s</p>" ($$ cache-control))))
              "Not Found"))

  ;; Multiple handlers under a single route

  (chp-route "/"
             (let [display (str (format "Method %s <br />" (escape ($ method)))
                                (format "URI %s <br />" (escape ($ uri)))
                                (format "Params %s <br />" (escape ($ params)))
                                (format "Header Values <p>%s</p>"
                                        (with-out-str
                                          (doseq [[k v] (escape-map ($ headers))]
                                            (println k "=" v "<br />"))))
                                (format "Server Name %s <br /> Server IP %s"
                                        ($ server-name)
                                        ($ server-ip)))]
               (chp-body {:-get (str "Get => " display)
                          :-post (str "Post => " display)
                          :-not-found "Sorry, but this page doesn't exist"})))

  ;; Bind to templates

  (chp-route "/template"
             (using-template "example.chtml"
                             {:body "chp-info.chtml"
                              :test-tag "test-page.chtml"}))

  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (chp-site example-routes
            user-table-routes
            chp-builder-paths
            app-routes))
            
  
