#### CHP [![endorse](https://api.coderwall.com/runexec/endorsecount.png)](https://coderwall.com/runexec)
ClojureHomePage is a Clojure Web Framework

This framework provides the following

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
* Perform SQL database migrations ex. lein migrate
* Perform migration rollbacks ex. lein rollback
* Manipulate SQL databases with KormaSQL

<b> Code Generation </b>

* Page views (new,view,edit,list)
* Generate JavaScript / ECMAScript
* Generate HTML
* Generate CSS


This page serves as project documentation.<br />

* [Install](#getting-started)
* [CHTML & Routes](#example-chtml--routes)
* [Generating views from a table](#generating-table-views)
* [View bindings](#builder-bindings)
* [Admin view generation workflow](#builder-binding-views-example)
* [SQL DB configuration and creation](#db-configuration-and-creation)
* [SQL DB Migrations](#db-migrations)
* [SQL Manipulation](#clojure-and-sql)
* [HTML Generation](#clojure-and-html-generation)
* [CSS Generation](#clojure-and-css-generation)
* [JavaScript Generation](#clojure-and-javascript-generation)
* [Session handling, Cookies, and Compojure](#session-handling-cookies-and-compojure)
* [Ring and port configuration](#ring-configuration)
* [Removing example files](#removing-example-files)
* [License](#license)

Other Documentation 

1. [How?](#how)
2. [Tutorial](https://github.com/runexec/chp/tree/master/tutorial/01)

# Getting started

1) Download & Run

```bash
git clone https://github.com/runexec/chp
cd chp
lein ring server
```

2) Edit the default app-routes template located at the bottom of src/chp/handler.clj

# How?

By default, the CHTML files are located in chp-root folder of the project folder.
When a CHTML file is parsed, all public variables of the chp.handler namespace
are accessible during the evaluation of the ```<clj></clj>``` tags. Use print
 or println within the tags to have the results displayed.


# Example CHTML & Routes

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
               (or (root-parse "test-page.chtml")
                   "error")))
  (chp-route "/chp"
	     ;; root-parse = root-path "/" file
             (or (root-parse "chp-info.chtml")
                 "error"))
  (chp-route "/session"
            (or (root-parse "session-example.chtml")
                "error"))

  ;; Named params

  (chp-route "/index/:id"
             (format "ID is %s" 
                     (escape ($p id))))
  (chp-route "/index/:id/:action"
             (format "Action is %s" 
                     (escape ($p action))))

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

  ;; Bind to templates

  (chp-route "/template"
             (using-template "example.chtml"
                             {:body "chp-info.chtml"
                              :test-tag "test-page.chtml"}))

  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (chp-site example-routes
            app-routes))
```


# Clojure and HTML Generation

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

# Clojure and CSS Generation

ClojureHomePage uses the Garden CSS generation library by default.
The Garden documentation page is located at https://github.com/noprompt/garden

# Clojure and JavaScript Generation

ClojureHomePage uses ClojureScript and lein-cljsbuild to generate javascript.
CHP uses the directory resources/cljs/ as the default cljs source code directory.

1. [lein-cljsbuild Documentation](https://github.com/emezeske/lein-cljsbuild/)
2. [ClojureScript Documentation](https://github.com/clojure/clojurescript)

# DB Configuration and Creation

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

# DB Migrations

<b> Perform migration </b>

```clojure
$ lein migrate
add-topic-table
add-topic-subject-table
add-tag-table
```

<b> Lobos migration files </b>

```bash
$ cat resources/migrations/01-add-topic-tables.clj
```
```clojure
(defmigration add-topic-table
  (up [] (create
          (tbl :topic
                 (varchar :title 50 :unique)
                 (text :content))))
  (down [] (drop (table :topic))))

(defmigration add-topic-subject-table
  (up [] (create
          (tbl :topicSubject
                 (varchar :title 50 :unique)
                 (integer :id :auto-inc :primary-key))))
  (down [] (drop (table :topicSubject))))
```
```bash 
$ cat resources/migrations/02-add-tag-table.clj
```
```clojure
(defmigration add-tag-table
  (up [] (create
          (tbl :tag
               (varchar :title 25)
               (integer :id :auto-inc :primary-key))))
  (down [] (drop (table :tag))))
```

<b> Tables after migration </b>

```bash
example=# \dt
             List of relations
 Schema |       Name       | Type  | Owner 
--------+------------------+-------+-------
 public | example          | table | on
 public | lobos_migrations | table | on
 public | tag              | table | on
 public | topic            | table | on
 public | topicSubject     | table | on
 public | user             | table | on
(6 rows)
```

<b> Rollbacks </b>

```bash
$ lein rollback
add-tag-table
$ lein rollback
add-topic-subject-table
```

<b> Tables after rollback </b>

```bash
example-# \dt
             List of relations
 Schema |       Name       | Type  | Owner 
--------+------------------+-------+-------
 public | example          | table | on
 public | lobos_migrations | table | on
 public | topic            | table | on
 public | user             | table | on
(4 rows)

example-# 
```

1. [Lobos Project & Documentation](https://github.com/budu/lobos)
2. [More Lobos Documentation](http://budu.github.io/lobos/documentation.html)


# Builder Bindings

The example user.clj bindings below will be used to make the new, list, view, and edit pages of the user table in schema/user.clj.

Th below bindings will produce the following urls.

```
site.com/chp/list/user
site.com/chp/new/user
site.com/chp/view/user/1
site.com/chp/edit/user/1
```

```clojure
;; Example bindings for resources/schema/user.clj
;; All values will be retrieved by the id column

;; table must match the filename withut the clj extension
;; user.clj -> user

{:table :user

;; List view value
;; (chp.builder/binding-list :user 0 10)
;; /chp/list/user

 :list (list :name :id)

;; View view values
;; (chp.builder/binding->view :user 1)
;; site.com/chp/view/user/:id

 :view (list :name :password :admin)

;; Edit view values
;; (chp.builder/binding->edit :user 1)
;; site.com/chp/edit/user/:id 
;; site.com/chp/new/user

;; edit is a hash-set with table columns
;; as the key and the chp.html namespace
;; function used to display the value.

 :edit {:name #(text-field :name (escape %))
        :password #(password-field :password (escape %))
        :admin #(check-box :admin (Boolean/valueOf %))}

;; enforce data type with fn to check and
;; or convert before going into database.
;; The function must take one arg.

 :edit-enforce {:name str
                :password str
                :admin #(Boolean/valueOf %)}}
```

# Builder binding views example

```bash
$ cd chp
$ cat resources/schema/user.clj 
```
```clojure
(table :user
       (integer :id :primary-key :auto-inc)
       (varchar :name 20)
       (varchar :password 100)
       (boolean :admin)
       (unique [:name]))
```
```bash
$ lein schema
Creating Table =>  resources/schema/example.clj
OKAY
Creating Table =>  resources/schema/user.clj
OKAY
$ psql example
psql (9.2.4)
Type "help" for help.
example=# INSERT INTO "user" (name,password,admin) VALUES ('user1','badcleartext',true);
example=# \q
$ lein ring server &
Jun 24, 2013 3:40:47 PM com.mchange.v2.log.MLog <clinit>
INFO: MLog clients using java 1.4+ standard logging.
2013-06-24 15:40:47.999:INFO:oejs.Server:jetty-7.6.1.v20120215
2013-06-24 15:40:48.064:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:8000
Started server on port 8000

#  In another terminal  ###

$ telnet localhost 8000
Trying ::1...
Connected to localhost.
Escape character is '^]'.
GET /chp/list/user


<h1>Viewing table of  user
</h1>

<div style="background:yellow;">
  <table><thead><tr><th>Action</th><th><b>id</b></th><th><b>name</b></th></tr></thead>
<tr><td><a href="/chp/view/user/1">view</a> <a href="/chp/edit/user/1">edit</a></td><td>1</td><td>user1</td></tr>
</table>
<br /><br /> <a href="/chp/list/user?offset=10">More</a>

</div>
Connection closed by foreign host.



$ telnet localhost 8000
Trying ::1...
Connected to localhost.
Escape character is '^]'.
GET /chp/view/user/1
<h1>Viewing  1
</h1>

[name user1]
[password badcleartext]
[admin true]

Connection closed by foreign host.



$ telnet localhost 8000
Trying ::1...
Connected to localhost.
Escape character is '^]'.
GET /chp/edit/user/1

<h1> Editing user #1 </h1>

<form action="/chp/edit/user/1
" method="POST">
<label for=":admin">admin</label><br /><input checked="checked" id="admin" name="admin" type="checkbox" value="true" /><br /><br /><label for=":password">password</label><br /><input id="password" name="password" type="password" value="badcleartext" /><br /><br /><label for=":name">name</label><br /><input id="name" name="name" type="text" value="user1" /><br /><br /> <input type="submit" value="save" />

</form>
Connection closed by foreign host.
```

# Generating Table Views

```bash
$ lein gen user
resources/generation-templates/routes/name.clj -> src/chp/routes/user.clj
resources/generation-templates/chtml/new.chtml -> chp-root/user/new.chtml
resources/generation-templates/chtml/edit.chtml -> chp-root/user/edit.chtml
resources/generation-templates/chtml/view.chtml -> chp-root/user/view.chtml
resources/generation-templates/chtml/list.chtml -> chp-root/user/list.chtml
URL DATA BOUND TO => resources/bindings/user.clj 
site.com/new/user 
site.com/list/user 
site.com/edit/user/:id 
site.com/view/user/:id
$ cat resources/bindings/user.clj
```
```clojure
;; Example bindings for resources/schema/user.clj
;; All values will be retrieved by the id column

;; table must match the filename withut the clj extension
;; user.clj -> user

{:table :user

;; List view value
;; (chp.builder/binding-list :user 0 10)
;; /chp/list/user

 :list (list :name :id)

;; View view values
;; (chp.builder/binding->view :user 1)
;; site.com/chp/view/user/:id

 :view (list :name :password :admin)

;; Edit view values
;; (chp.builder/binding->edit :user 1)
;; site.com/chp/edit/user/:id 
;; site.com/chp/new/user

;; edit is a hash-set with table columns
;; as the key and the chp.html namespace
;; function used to display the value.

 :edit {:name #(text-field :name (escape %))
        :password #(password-field :password (escape %))
        :admin #(check-box :admin (Boolean/valueOf %))}

;; enforce data type with fn to check and
;; or convert before going into database.
;; The function must take one arg.

;; :name is limited to a string of 20 chars
;; :password is limited to 100 chars
;; :admin mut be a boolean value
 :edit-enforce {:name #(->> % str seq (take 20) (apply str))
                :password  #(->> % str seq (take 100) (apply str))
                :admin #(Boolean/valueOf %)}}
```
             
# Clojure and SQL 

ClojureHomePage uses the SQLKorma DSL by default. korma.db is required as kdb and korma.core is required as kc

1. [Korma Documentation](http://www.sqlkorma.com/)

# Session handling, Cookies, and Compojure

Sessions are handled with the lib-noir.session namespace under the session alias.

* [lib-noir session API](https://yogthos.github.io/lib-noir/noir.session.html)

This session example can be accessed at site.com/session
```clojure
You have viewed this page 

<clj>
(let [k :view-count
      inc-view (k (session/update-in! [k] inc))]
  (print inc-view))
</clj>

time(s).
```

Because CHP is based on Compojure, you can use Compojure and Ring extensions. These middleware extensions should be added to the chp-routing function of the chp.core namespace. Below is what the function currently looks like.
```clojure
(defn chp-routing [& -chp-routes]
  ;;; (-> (apply routes ...) middleware-wrap xyz-wrap)
  (-> (apply routes
             (reduce into [] -chp-routes))
      wrap-noir-flash
      wrap-noir-session))
```

 Already included, but not loaded by default (except noir.session), the lib-noir library is a great helper library for Clojure web development.


1. [lib-noir API](http://yogthos.github.io/lib-noir/index.html)
2. [lib-noir Github](https://github.com/noir-clojure/lib-noir)
3. [Ring CSRF protection](https://github.com/weavejester/ring-anti-forgery)
4. [Ring Middleware Extensions](https://github.com/search?q=ring+middleware&ref=cmdform&type=Repositories)

# Ring configuration

The default configuration for CHP is located in project.clj

```clojure
:ring {:port 8000
       :auto-reload? true
       :auto-refresh? true
       :reload-paths ["src/chp/"]
       :handler chp.handler/app}
```

1. [Lein-ring documentation](https://github.com/weavejester/lein-ring)


# Removing Example Files

```bash
$ ls 
chp-examples/  chp-root/  resources/  src/  target/  test/  tutorial/  project.clj README.md
$ lein chp-clean
Removing README.md
Removing resources/schema
Removing resources/schema/example.clj
Removing resources/schema/user.clj
Removing resources/bindings
Removing resources/bindings/user.clj
Removing resources/migrations
Removing resources/migrations/02-add-tag-table.clj
Removing resources/migrations/01-add-topic-tables.clj
$ ls
chp-root/  resources/  src/  target/  test/  project.clj
```

# License

Copyright © 2013 Runexec

Distributed under the Eclipse Public License, the same as Clojure.
