#### CHP
Clojure web routing and template framework based on Compojure

You can <br />
* Embed Clojure into a HTML file with the ```<clojure></clojure>``` tags
* Enable dynamic route method handling (get, post, put, delete, and head)


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
are accessible during the evaluation of the ```<clojure>/clojure>``` tags. Use print
 or println within the tags to have the results displayed.


#### Example CHTML & Routes

The following link is the chtml page that is used in the example below. 
<a href="https://github.com/runexec/chp/blob/master/chp-root/test-page.chtml">
   test-page.chtml
</a>


```clojure
(defroutes app-routes
  (chp-route "/chtml" 
  	     ;; access dynamic variables in chp.handler namespace
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

(def app
  (handler/site app-routes))
```

