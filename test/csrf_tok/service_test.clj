(ns csrf-tok.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [io.pedestal.log :as log]
            [csrf-tok.service :as service]
            [clojure.string :refer [starts-with? split]]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(deftest csrf-test
  (let [{:keys [status]
         :as response} (response-for service :post "/csrf")]
    (is (= 403 status))))

(deftest csrf-cookie-test
  (let [{:keys [status]
         :as response} (response-for service :post "/csrf-cookie")]
    (is (= 403 status))))
