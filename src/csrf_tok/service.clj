(ns csrf-tok.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.ring-middlewares :as middlewares]
            [io.pedestal.interceptor :refer [interceptor]]
            [io.pedestal.http.csrf :as csrf]
            [io.pedestal.log :as log]
            [ring.util.response :as ring-resp]))

(defn home-page
  [request]
  (ring-resp/response "Hello World!\n"))

(defn cookie-session
  [{:keys [session] :as request}]
  (-> (ring-resp/response "Hello World!\n")
      (assoc :session session)))

(def common-interceptors [middlewares/cookies
                          (middlewares/session {})
                          (body-params/body-params)
                          http/html-body])

(def csrf-interceptors [middlewares/cookies
                        (middlewares/session {})
                        (csrf/anti-forgery)
                        (body-params/body-params)
                        http/html-body])

(def csrf-cookie-interceptors [middlewares/cookies
                               (middlewares/session {})
                               (csrf/anti-forgery {:cookie-token true})
                               (body-params/body-params)
                               http/html-body])

(def routes #{["/" :get (conj common-interceptors `home-page)]
              ["/" :post (conj common-interceptors `home-page)
               :route-name :post-home-page]
              ["/csrf" :get (conj csrf-interceptors `home-page)
               :route-name :get-csrf]
              ["/csrf" :post (conj csrf-interceptors `home-page)
               :route-name :post-csrf]
              ["/csrf-cookie" :get (conj csrf-cookie-interceptors `home-page)
               :route-name :get-csrf-cookie]
              ["/csrf-cookie" :post (conj csrf-cookie-interceptors `home-page)
               :route-name :post-csrf-cookie]})


(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})
