(ns chp.html
  (:require hiccup.core
            hiccup.util
            hiccup.form
            hiccup.element))

(defn render [& hiccup-bodies]
  (hiccup.core/html hiccup-bodies))

(defn escape [s]
  (hiccup.core/h s))

(defn escape-map [map-coll]
  (zipmap (map escape (keys map-coll))
          (map escape (vals map-coll))))

(defn url-encode [s]
  (hiccup.util/url-encode s))

(defmacro render-element [e & hiccup-args]
  `(->> ~'hiccup-args
        (apply ~e)
        render))

(defmacro re
  "alias for render-element"
  [e & ha]
  `(render-element ~e ~'ha))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Form Fields

(defn check-box 
  "(check-box attr-map? name)
   (check-box attr-map? name checked?)
   (check-box attr-map? name checked? value)"
  [& hiccup-args]
  (re hiccup.form/check-box
      hiccup-args))

(defn drop-down
  "(drop-down attr-map? name options)
   (drop-down attr-map? name options selected)"
  [& hiccup-args]
  (re hiccup.form/drop-down
      hiccup-args))

(defn email-field
  "(email-field attr-map? name)
   (email-field attr-map? name value)"
  [& hiccup-args]
  (re hiccup.form/email-field
      hiccup-args))

(defn file-upload
  "(file-upload attr-map? name)"
  [& hiccup-args]
  (re hiccup.form/file-upload
      hiccup-args))

(defn form-to
  "(form-to attr-map? [method action] & body)"
  [& hiccup-args]
  (re hiccup.form/form-to
      hiccup-args))

(defn hidden-field
  "(hidden-field attr-map? name)
   (hidden-field attr-map? name value)"
  [& hiccup-args]
  (re hiccup.form/hidden-field
      hiccup-args))

(defn label
  "(label attr-map? name text)"
  [& hiccup-args]
  (re hiccup.form/label
      hiccup-args))

(defn password-field
  "(password-field attr-map? name)
   (password-field attr-map? name value)"
  [& hiccup-args]
  (re hiccup.form/password-field
      hiccup-args))

(defn radio-button
  "(radio-button attr-map? group)
   (radio-button attr-map? group checked?)
   (radio-button attr-map? group checked? value)"
  [& hiccup-args]
  (re hiccup.form/radio-button
      hiccup-args))

(defn reset-button
  "(reset-button attr-map? text)"
  [& hiccup-args]
  (re hiccup.form/radio-button
      hiccup-args))

(defn select-options
  "(select-options attr-map? coll)
   (select-options attr-map? coll selected)"
  [& hiccup-args]
  (re hiccup.form/select-options
      hiccup-args))

(defn submit-button
  "(submit-button attr-map? text)"
  [& hiccup-args]
  (re hiccup.form/submit-button
      hiccup-args))

(defn text-area
  "(text-area attr-map? name)
   (text-area attr-map? name value)"
  [& hiccup-args]
  (re hiccup.form/text-area
      hiccup-args))

(defn text-field
  "(text-field attr-map? name)
   (text-field attr-map? name value)"
    [& hiccup-args]
    (re hiccup.form/text-field
        hiccup-args))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Common Elements

(defn javascript-tag
  "(javascript-tag script)"
    [& hiccup-args]
    (re hiccup.element/javascript-tag
        hiccup-args))

(defn image
  "(image attr-map? src)
   (image attr-map? src alt)"
    [& hiccup-args]
    (re hiccup.element/image
        hiccup-args))

(defn link-to
  "(link-to attr-map? url & content)"
    [& hiccup-args]
    (re hiccup.element/link-to
        hiccup-args))

(defn mail-to
  "(mail-to attr-map? e-mail & [content])"
  [& hiccup-args]
  (re hiccup.element/mail-to
      hiccup-args))

(defn ordered-list
  "(ordered-list attr-map? coll)"
  [& hiccup-args]
  (re hiccup.element/ordered-list
      hiccup-args))


(defn unordered-list
  "(unordered-list attr-map? coll)"
  [& hiccup-args]
  (re hiccup.element/unordered-list
      hiccup-args))
