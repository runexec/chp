(defn verbose-log [handler]
  (fn [request]
    (let [resp (handler request)
          {:keys [remote-addr
                  request-method
                  uri
                  headers]} request
          ua (get headers "user-agent")
          time (str (java.util.Date.))
          log (format "%s - %s - %s - %s - %s"
                      time
                      remote-addr
                      (name request-method)
                      uri
                      ua)]
      (println log)
      resp)))
