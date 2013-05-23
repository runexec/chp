(ns chp.server)

(defn ^:private -lh [] (java.net.InetAddress/getLocalHost))

(defn addr [] 
  (as-> (-lh) host-ip
        (str host-ip)
        (.. host-ip (split "/"))
        (last host-ip)))

(defn host-name [] (.getHostName (-lh)))


