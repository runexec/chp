(ns chp.clean)

(def dirs
  ["chp-examples/"
   "tutorial/"])

(defn- remove-directories [dirs]
  (doseq [d dirs
          :let [cmd (str "rm -rf " d)]]
    (.. (java.lang.Runtime/getRuntime)
        (exec cmd))))

(def files ["README.md"])

(def file-paths
  ["resources/schema"
   "resources/bindings"
   "resources/migrations"])

(defn- remove-files [files file-paths]
  (let [-files (for [f file-paths]
                 (for [fp (-> f clojure.java.io/file file-seq)]
                         fp))
        -files (into -files (map clojure.java.io/file files))
        -files (flatten -files)]
    (doseq [fp -files
            :let [path (.. fp getPath)]]
      (println "Removing" path)
      (.. fp delete))
    (doseq [fp (map clojure.java.io/file file-paths)]
      (.. fp mkdir))))

(defn clean []
  (remove-files files file-paths)
  (remove-directories dirs))
