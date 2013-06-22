#### CHP [![endorse](https://api.coderwall.com/runexec/endorsecount.png)](https://coderwall.com/runexec)
ClojureHomePage is a Compojure based web framework that allows you to write the backend and frontend with Clojure.


This library provides the following

<b> Clojure on the front end </b>

* Run Clojure inside a HTML file with the ```<clj></clj>``` tags
* Style templates can be written in CHTML ex. chp.template/using-template

<b> Parameters  </b>

* Request params ex. ($p userid)
* Common web headers ex. ($ user-agent)
* Web Headers ex. ($$ cache-control)
* Environmental variables ex. (env java.vm.name)

<b> Path Routing </b>

* Have multiple method handlers under a single route (get, post, put, delete, and head)
* Routes can be defined in seperate files and namespaces

<b> DB </b>

* Create SQL database schemas ex. lein schema
* Manipulate SQL databases

<b> Code Generation </b>

* Generate JavaScript / ECMAScript
* Generate HTML
* Generate CSS


This page serves as project documentation.<br />

1. [Install](#getting-started)
2. [CHTML & Routes](#example-chtml--routes)
3. [SQL DB configuration and creation](#db-configuration-and-creation)
4. [SQL Manipulation](#clojure-and-sql)
5. [HTML Generation](#clojure-and-html-generation)
6. [CSS Generation](#clojure-and-css-generation)
7. [JavaScript Generation](#clojure-and-javascript-generation)
8. [Session handling, Cookies, and Compojure](#session-handling-cookies-and-compojure)
9. [Ring and port configuration](#ring-configuration)

Other Documentation 

1. [How?](#how)
2. [Tutorial](https://github.com/runexec/chp/tree/master/tutorial/01)

#### Getting started

1) Download & Run

```bash
git clone https://github.com/runexec/chp
cd chp
lein ring server
```

2) Edit the default app-routes template located at the bottom of src/chp/handler.clj

#### How?

By default, the CHTML files are located in chp-root folder of the project folder.
When a CHTML file is parsed, all public variables of the chp.handler namespace
are accessible during the evaluation of the ```<clj></clj>``` tags. Use print
 or println within the tags to have the results displayed.


#### Example CHTML & Routes

Routes can be stored in two places

1. File: src/chp/handler.clj
2. Folder: src/chp/routes/

The following link is the chtml page that is used in the example below. 
<a href="https://github.com/runexec/chp/blob/master/chp-root/test-page.chtml">
   test-page.chtml
</a>

<b> Routes Example </b>


```clojure
(defchp app-routes

  ;; Load CHP File

  (chp-route "/chtml" 
             (binding [*title* "Test Page Example"]
               (or (chp-parse (str root-path "test-page.chtml"))
                   "error")))
  (chp-route "/chp"
             (or (chp-parse (str root-path "chp-info.chtml"))
                 "error"))

  ;; Bind to templates

  (chp-route "/template"
             (using-template "example.chtml"
                             {:body "chp-info.chtml"
                              :test-tag "test-page.chtml"}))

  ;; Multiple handlers under a single route

  (chp-route "/"
             (let [display (str (format "Method %s <br />" (escape ($ method)))
                                (format "URI %s <br />" (escape ($ uri)))
                                (format "Params %s <br />" (escape ($ params)))
                                (format "Header Values <p>%s</p>"
                                        (with-out-str
                                          (doseq [[k v] (escape-map ($ headers))]
                                            (println k "=" v "<br />"))))
                                (format "Server Name %s <br /> Server IP %s"
                                        ($ server-name)
                                        ($ server-ip)))]
               (chp-body {:-get (str "Get => " display)
                          :-post (str "Post => " display)
                          :-not-found "Sorry, but this page doesn't exist"})))

  ;; Multiple handlers under a single route

  (chp-route "/testing"
             (or 
              (chp-when :post "POST METHOD RETURN")
              (chp-when :get
                        (str (format "chp-body wasn't used to access %s from %s with %s"
                                     ($ uri) ($ ip) ($ user-agent))
                             (format "<p>Tracking you? DNT HTTP Header = %s</p>" ($$ dnt))
                             (format "<p>HTTP Header cache-control = %s</p>" ($$ cache-control))))
                 "Not Found"))

  ;; Named params

  (chp-route "/index/:id"
             (format "ID is %s" 
                     (escape ($p id))))
  (chp-route "/index/:id/:action"
             (format "Action is %s" 
                     (escape ($p action))))


  (route/resources "/")
  (route/not-found "Not Found"))



(def app
  (chp-site example-routes
            app-routes))
```


#### Clojure and HTML Generation

The following methods presented in the documentation below are 
accessible from within CHTML files by default. These abstractions
are drop-in replacements for the Hiccup API located at http://weavejester.github.io/hiccup/.
Please note that these forms DO NOT generate Hiccup code, but HTML.


```clojure
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Form Fields

(escape string)

(url-encode string)

(check-box attr-map? name)
(check-box attr-map? name checked?)
(check-box attr-map? name checked? value)

(drop-down attr-map? name options)
(drop-down attr-map? name options selected)

(email-field attr-map? name)
(email-field attr-map? name value)

(file-upload attr-map? name)

(form-to attr-map? [method action] & body)

(hidden-field attr-map? name)
(hidden-field attr-map? name value)

(label attr-map? name text)

(password-field attr-map? name)
(password-field attr-map? name value)

(radio-button attr-map? group)
(radio-button attr-map? group checked?)
(radio-button attr-map? group checked? value)

(reset-button attr-map? text)

(select-options attr-map? coll)
(select-options attr-map? coll selected)

(submit-button attr-map? text)

(text-area attr-map? name)
(text-area attr-map? name value)

(text-field attr-map? name)
(text-field attr-map? name value)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Common Elements

(javascript-tag script)

(image attr-map? src)
(image attr-map? src alt)

(link-to attr-map? url & content)

(mail-to attr-map? e-mail & [content])

(ordered-list attr-map? coll)

(unordered-list attr-map? coll)

```

#### Clojure and CSS Generation

ClojureHomePage uses the Garden CSS generation library by default.
The Garden documentation page is located at https://github.com/noprompt/garden

#### Clojure and JavaScript Generation

ClojureHomePage uses ClojureScript and lein-cljsbuild to generate javascript.
CHP uses the directory resources/cljs/ as the default cljs source code directory.

1. [lein-cljsbuild Documentation](https://github.com/emezeske/lein-cljsbuild/)
2. [ClojureScript Documentation](https://github.com/clojure/clojurescript)

#### DB Configuration and Creation

A Korma SQL and Lobos compatible SQL connection configuration file is located at resources/config/db.clj

The SQL database tables are located in resources/schema/. Each file represents a single table and these files get evaluated by the lein alias ```lein schema```.

```clojure
$ lein schema
Creating Table =>  resources/schema/example.clj
OKAY
Creating Table =>  resources/schema/user.clj
OKAY
```

The Lobos library handles the table syntax. Below is the user table from user.clj.

```clojure
(table :user
       (integer :id :primary-key :auto-inc)
       (varchar :name 20)
       (varchar :password 100)
       (unique [:name]))
```

*Do not wrap your tables with the create macro.*

1. [Lobos Project & Documentation](https://github.com/budu/lobos)
2. [More Lobos Documentation](http://budu.github.io/lobos/documentation.html)

#### Clojure and SQL 

ClojureHomePage uses the SQLKorma DSL by default. korma.db is required as kdb and korma.core is required as kc

1. [Korma Documentation](http://www.sqlkorma.com/)

#### Session handling, Cookies, and Compojure

Because CHP is Compojure based, you can use Compojure and Ring extensions. Already included, but not loaded by default, the lib-noir library is a great helper library for Clojure web development.


1. [lib-noir API](http://yogthos.github.io/lib-noir/index.html)
2. [lib-noir Github](https://github.com/noir-clojure/lib-noir)
3. [Ring CSRF protection](https://github.com/weavejester/ring-anti-forgery)
4. [Ring Middleware Extensions](https://github.com/search?q=ring+middleware&ref=cmdform&type=Repositories)

#### Ring configuration

The default configuration for CHP is located in project.clj

```clojure
:ring {:port 8000
       :auto-reload? true
       :auto-refresh? true
       :reload-paths ["src/chp/"]
       :handler chp.handler/app}
```

1. [Lein-ring documentation](https://github.com/weavejester/lein-ring)

