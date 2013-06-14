(ns chp.template
  (:use chp.core))

(defn with-template
  "Takes map of chtml paths. The key must be a keyword, and
   the key must represent a place holder in the template.

   ex. {:body \"/path/file.chtml\"} will replace {{body}}
   found inside of file.chtml"
  [template-path -map]
  {:pre (every? keyword? (keys -map))}
  (let [ks (into [] (keys -map))
        vs (mapv #(-> % chp-path chp-parse) (vals -map))
        html-map (zipmap ks vs)
        replacement #(format "{{%s}}" (name %))
        template (slurp template-path)]
    (loop [-keys ks
           body template]
      (let [k (last -keys)]
        (if-not k
          body
          (let [r (replacement k)
                html (k html-map)]
          (recur
           (drop-last -keys)
           (.. body (replace r html)))))))))


(defn using-template [file-name -map]
  (with-template (chp-template-path file-name) -map))
                           
