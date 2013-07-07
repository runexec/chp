;; This file is automatically loaded as middleware
;; and should only contain one function.

(defn example-middleware [handler]
  (fn [request]
    (let [resp (handler request)
          headers (:headers resp)]
      (println "resources/middleware/example.clj says "
               "- Incoming request >> " headers)
      resp)))
