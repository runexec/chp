(defn text-mime-only [handler]
  (fn [req]
    (let [resp (handler req)
          header "Content-Type"
          -type (as-> (:headers resp) h
                      (get h header))]
      (if (re-find #"text/\w+" -type)
        resp
        (-> resp
            (assoc-in [:headers header] "text/plain;")
            (assoc :status 403
                   :body "text request only"))))))
          
      
