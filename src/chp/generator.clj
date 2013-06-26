(ns chp.generator
  (:use [chp.core
         :only [root-path]]))


(def chp-routes-dir "src/chp/routes/")

(def routes-path "resources/generation-templates/routes/name.clj")

(defn routes->gen [table-name]
  (let [name (str table-name)
        namespace (.. name
                      (replace "_" "-")
                      toLowerCase)
        file-name (str chp-routes-dir name ".clj")]
    (println routes-path "->" file-name)
    (as-> routes-path x
          (slurp x)
          (.. x (replace "{{name}}" name))
          (.. x (replace "{{namespace}}" namespace))
          (spit file-name x))))

(def chtml-path "resources/generation-templates/chtml/")

(def chtml-files
  (let [fp (clojure.java.io/file chtml-path)]
    (for [f (file-seq fp)
          :when (.. f getName (endsWith ".chtml"))
          :let [f (.. f getName)]]
     f)))

(defn chtml->gen [table-name]
  (let [to-path (str root-path table-name "/")]
    (.. (clojure.java.io/file to-path) mkdir)
    (doseq [f chtml-files
            :let [from (str chtml-path f)
                  to (str to-path f)
                  data (as-> from x
                             (slurp x)
                             (.. x (replace "{{name}}" table-name)))]]
      (println from "->" to)
      (spit to data))))

(defn builder->gen [& table-names]
  (doseq [table-name table-names]
    (routes->gen table-name)
    (chtml->gen table-name)
    (println 
     (str "URL DATA BOUND TO => resources/bindings/" table-name ".clj")
     (str "\nsite.com/new/" table-name)
     (str "\nsite.com/list/" table-name)
     (str "\nsite.com/edit/" table-name "/:id")
     (str "\nsite.com/view/" table-name "/:id"))))
