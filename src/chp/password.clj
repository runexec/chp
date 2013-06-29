(ns chp.password)

(defn password [salt s]
  (let [password (str salt s)]
    (->> (doto (java.security.MessageDigest/getInstance 
                "SHA-512")
           .reset
           (.update (.getBytes password)))
         .digest
         (map #(format "%02x" %))
         (apply str))))

(defn salt
  ([] (salt
       (reduce #(password %2 %1)
               (take 10
                     (iterate rand-int 100)))))
  ([& [base]]
     (if base
       (password base base)
       base)))
               
                    
