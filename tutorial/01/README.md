# Tutorial 1

By the end of this tutorial you should beable to do the following
* create a CHP project
* create a CHP CHTML page
* define a CHP route

#### Create a CHP project

1. Clone the repository 
```bash
git clone https://github.com/runexec/chp
```
2. CHP now sits in the directory chp, but you do not want to modify this copy. 
You should copy the chp folder into another folder holding the name of your project.
```bash
mkdir hello-world
cp -r chp/ hello-world/
tree -d hello-world/
```
<pre>
hello-world/
└── chp
    ├── chp-root
    ├── resources
    │   └── public
    ├── src
    │   └── chp
    ├── target
    │   ├── classes
    │   └── stale
    ├── test
    │   └── chp
    │       └── test
    └── tutorial
        └── 01
</pre>

3. Your hello-world CHP project is now setup correctly and you can run the ring server.
```bash
cd hello-world/chp/; lein ring server
2013-05-09 12:46:45.889:INFO:oejs.Server:jetty-7.6.1.v20120215
2013-05-09 12:46:45.947:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:3000
Started server on port 3000
```

#### Create a CHP CHTML Page

1. First thing we're going to do is delete the contents of ```hello-world/chp-root/test-file.chtml```
2. In the same file, paste the following HTML code into the CHTML file.
```html

```html
<html>
  <head>
    <title></title>
  </head>
  <body>
  </body>
</html>
```
3. To access this blank page, you can visit http://localhost:3000/chtml
4. Let's give this page an interesting title.
```
<html>
  <head>
    <title>
      <clojure>
      (println 
         (format "%s's ClojureHomePage!"
                 (escape
                  (System/getProperty "user.name"))))
      </clojure>
    </title>
  </head>
  <body>
  </body>
</html>
```
 If you did everything correctly, the title should display your current
 logged in username before displaying "ClojureHomePage!".
5. Now let's have a little fun with the CHTML body.
```clojure
<html>
  <head>
    <title>
      <clojure>
      (println 
         (format "%s's ClojureHomePage!"
                 (escape
                  (System/getProperty "user.name"))))
      </clojure>
    </title>
  </head>
  <body>
    <table>
      <tr>
      <td><h1>System Property</h1></td>
      <td><h1>Property Value</h1></td>
      </tr>
      <clojure>
      (doseq [_ (System/getProperties)
                :let [k (key _) 
                      v (System/getProperty k)]]
          (println 
           (format "<tr><td>%s</td><td>%s</td>"
                   (escape k)
                   (escape v))))
      </clojure>
    </table>
  </body>
</html>
```
#### Define a CHP route

1. Now that you've created your own CHTML page, let's start to give it a better route by opening the file ```hello-world/chp/src/chp/handler.clj```
2. You'll want to scroll to the very bottom of the file or until you see ```(defroutes app-routes ...)```
3. Listed here are all the possible ways to make a CHP route, but we only need a simple one this example.
```clojure
(defroutes app-routes
  (chp-route "/chtml" 
             (binding [*title* "Test Page Example"]
               (or (chp-parse (str root-path "test-page.chtml"))
                   "error")))
  (chp-route "/"
             (let [display (str "Method " ($ method) "<br />"
                                "URI " ($ uri) "<br />"
                                "Params " ($ params) "<br />")]
               (chp-body {:-get (str "Get => " display)
                          :-post (str "Post => " display)
                          :-not-found "Sorry, but this page doesn't exist"})))
  (chp-route "/testing"
             (or (chp-when :get
                           (str "chp-body wasn't used to access "
                                ($ uri)))
                 "Not Found"))
  (route/resources "/")
  (route/not-found "Not Found"))
```
4. Let's make the simplest possible route we can for the location of "/"
```clojure
(defroutes app-routes
  (chp-route "/"
             (or (chp-parse (str root-path "test-page.chtml"))
                 "Page not found!"))
  (route/resources "/")
  (route/not-found "Not Found"))
```
5. After saving the changes, point your browser to http://localhost:3000/

<a href="test-page.chtml">Download the CHTML used in this tutorial</a>
