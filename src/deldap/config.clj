(ns deldap.config
  (:require [aero.core :refer [read-config]]
            [mount.core :refer [defstate]]))

(defn load-config []
  (read-config "config.edn"))

(defstate config :start (load-config))
