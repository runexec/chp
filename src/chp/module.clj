(ns chp.module
  (:require [clojure.java.io :as io]))

(def mod-path "resources/modules/")

(defn- mpath-copy
  "types: :middleware :migration :schema :cljs :binding
   file: is a :name without the .clj extension"
  [-type -file & [enable?]]
  (let [-type (if (keyword? -type) (name -type) (str -type))
        -file (if (keyword? -file) (name -file) (str -file))
        from-path (format "%s%s/%s.clj" mod-path -type -file)
        to-path (format "resources/%s/%s.clj" -type -file)
        enable-message (format "Copying %s -> %s" from-path to-path)
        dissable-message (str "Deleting " to-path)]
    (if enable?
      (do (println enable-message)
          (io/copy (io/file from-path) (io/file to-path)))
      (do (println dissable-message)
          (.. (io/file to-path) delete)))))

(defn mod-enable 
  "All args are keywords and used to call mpath-copy"
  [-type & mod-names]
  (let [t (keyword -type)
        m (->> mod-names
               (map #(if-not (or (string? %)
                                 (keyword? %))
                       (str %)
                       (keyword %)))
               (map keyword))]
    (doseq [_ m] (mpath-copy t _ true))))

(defn mod-disable
  "All args are keywords and used to call mpath-copy"
  [-type & mod-names]
  (let [t (keyword -type)
        m (->> mod-names
               (map #(if-not (or (string? %)
                                 (keyword? %))
                       (str %)
                       (keyword %)))
               (map keyword))]
    (doseq [_ m] (mpath-copy t _))))

(defn mod-list [] 
  (doseq [fp (file-seq (io/file mod-path))
          :let [n (.. fp getPath)]
          :when (or (.. fp isDirectory)
                    (.. fp getName (endsWith ".clj")))]
    (println n)))
