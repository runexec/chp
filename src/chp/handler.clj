(ns chp.handler
  (:use compojure.core
        chp.core
        chp.html
        chp.template
        [garden.core :only [css]])
  (:require chp.server
            [compojure.route :as route]
            [korma.db :as kdb]
            [korma.core :as kc]

            ;;;;;;;;;;;;;;;;;;; Routes

            [chp.routes.example
             :refer [example-routes]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Routes

(defchp app-routes

  ;; Load CHP File

  (chp-route "/chtml" 
             (binding [*title* "Test Page Example"]
               (or (chp-parse (str root-path "test-page.chtml"))
                   "error")))
  (chp-route "/chp"
             ;; root-parse = (chp-parse (str root-path path))
             (or (root-parse "chp-info.chtml")
                 "error"))

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

  ;; Named params

  (chp-route "/index/:id"
             (format "ID is %s" 
                     (escape ($p id))))
  (chp-route "/index/:id/:action"
             (format "Action is %s" 
                     (escape ($p action))))

  ;; Bind to templates

  (chp-route "/template"
             (using-template "example.chtml"
                              {:body "chp-info.chtml"
                               :test-tag "test-page.chtml"}))
                              

  (route/resources "/")
  (route/not-found "Not Found"))



(def app
  (chp-site example-routes
            app-routes))
  
