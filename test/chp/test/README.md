```clojure
chp.test.builder> (doseq [_ ['chp.test.builder
			     'chp.test.password
			     'chp.test.login
			     'chp.test.core]]
			 (clojure.pprint/pprint
			  (clojure.test/run-tests _)))

Testing chp.test.builder

Ran 10 tests containing 14 assertions.
0 failures, 0 errors.
{:type :summary, :pass 14, :test 10, :error 0, :fail 0}

Testing chp.test.password

Ran 3 tests containing 5 assertions.
0 failures, 0 errors.
{:type :summary, :pass 5, :test 3, :error 0, :fail 0}

Testing chp.test.login

Ran 5 tests containing 5 assertions.
0 failures, 0 errors.
{:type :summary, :pass 5, :test 5, :error 0, :fail 0}

Testing chp.test.core

Ran 12 tests containing 26 assertions.
0 failures, 0 errors.
{:type :summary, :pass 26, :test 12, :error 0, :fail 0}
nil
```