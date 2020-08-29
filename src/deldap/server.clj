(ns deldap.server
  (:use ring.adapter.jetty)
  (:require [deldap.ldap :as ldap]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :as r])
  (:gen-class))

(defn json [body]
  (-> (r/response body)
      (r/content-type "application/json")))

(def api-routes
  (routes
   (context "/api/v1" []
            (GET "/health" []
                 (json {:status :ok}))
            (GET "/children" [dn :as {conn :user-conn}]
                 (json (ldap/get-children conn dn))))))

(defn start-server [config]
  (-> api-routes
      (handler/api)
      (ldap/with-connection ldap/conn (select-keys (:ldap config) [:bind-dn :password]))
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :post :options])
      (run-jetty (:server config))))
