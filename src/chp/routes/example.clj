(ns chp.routes.example
  (:use chp.core
        compojure.core
        [chp.html :only [escape]]))

(defchp example-routes
  (chp-route "/example/:id" (str ($ params)))
  (GET "/example/:id/:action" {:keys [headers
                                      params
                                      body
                                      remote-addr
                                      request-method
                                      uri]}
      (str "CHTML can run in a regular compojure route, "
           "but no access to ($) ($$) forms."
           "<br />Action is "
           (escape (:action params)))))
