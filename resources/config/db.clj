;; This configuration is compatible with both the
;; Korma and Lobos SQL libraries.

;; SQLite doesn't play nice with lobos migrations
{:classname "org.postgresql.Driver"
 :subprotocol "postgresql"
 :subname "//localhost:5432/example" ;; db
 :user "on"}
