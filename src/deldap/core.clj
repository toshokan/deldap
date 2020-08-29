(ns deldap.core
  (:require [deldap.config :refer [config]]
            [deldap.server :as server]
            [mount.core :as mount])
  (:gen-class))

(defn -main [& args]
  (do
    (mount/start)
    (println config)
    (server/start-server config)))
