(ns chp.handler
  (:use compojure.core
        chp.core
        chp.html
        [garden.core :only [css]])
  (:require chp.server
            [compojure.handler :as handler]
            [compojure.route :as route]
            [korma.db :as kdb]
            [korma.core :as kc]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Routes

(defroutes app-routes
  (chp-route "/chtml" 
             (binding [*title* "Test Page Example"]
               (or (chp-parse (str root-path "test-page.chtml"))
                   "error")))
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
  (chp-route "/testing"
             (or (chp-when :get
                           (str (format "chp-body wasn't used to access %s from %s with %s"
                                        ($ uri) ($ ip) ($ user-agent))
                                (format "<p>Tracking you? DNT HTTP Header = %s</p>" ($$ dnt))
                                (format "<p>HTTP Header cache-control = %s</p>" ($$ cache-control))))
                 "Not Found"))
  (chp-route "/chp"
             (or (chp-parse (str root-path "chp-info.chtml"))
                 "error"))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
  
