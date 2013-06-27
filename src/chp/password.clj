(ns chp.password)

(defn password [salt s]
  (let [password (str salt s)]
    (->> (doto (java.security.MessageDigest/getInstance 
                "SHA-1")
           .reset
           (.update (.getBytes password)))
         .digest
         (map #(format "%02x" %))
         (apply str))))
