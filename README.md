# csrf-tok

Test case for showing csrf/anti-forgery with double-submit cookies returns a
404 Not Found rather than a 403 Forbidden if the request does not include
a valid CSRF token.

The `:leave` function of the `anti-forgery` interceptor returns `nil` (in
particular `assoc-double-submit-cookie` if there is no session set when using
cookie tokens.

## To reproduce

The pedestal service includes routes using csrf with (`/csrf-cookie`) and
without (`/csrf`) cookie tokens. The tests POST against these routes showing
`/csrf` responds with the expected 403 and `/csrf-cookie` responds with a 404.

    lein test

    lein test csrf-tok.service-test
    INFO  io.pedestal.http - {:msg "POST /csrf-cookie", :line 78}

    lein test :only csrf-tok.service-test/csrf-cookie-test

    FAIL in (csrf-cookie-test) (service_test.clj:20)
    expected: (= 403 status)
      actual: (not (= 403 404))
    INFO  io.pedestal.http - {:msg "POST /csrf", :line 78}

    Ran 2 tests containing 2 assertions.
    1 failures, 0 errors.
    Tests failed.
