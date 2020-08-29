(ns deldap.ldap
  (:require [clj-ldap.client :as ldap]
            [mount.core :refer [defstate]]
            [deldap.config :refer [config]])
  (:gen-class))

(defn get-conn [ldap-config]
  (ldap/connect (select-keys ldap-config [:host :ssl?])))

(defn with-connection [handler pool credentials]
  (fn [request]
    (let [conn (ldap/get-connection pool)]
      (try
        (ldap/bind? conn (:bind-dn credentials) (:password credentials))
        (handler (assoc request :user-conn conn))
        (finally (ldap/release-connection pool conn))))))

(defn get-children [conn base-dn]
  (ldap/search conn base-dn
               {:filter "(objectClass=ipaobject)"
                :attributes [:cn, :displayName, :uid, :objectClass]
                :scope :one
                :server-sort {:is-critical true :sort-keys [:uid :ascending]}}))

(defn get-group-members [conn group-dn]
  (or (ldap/search conn group-dn
               {:filter "(objectClass=groupofnames)"
                :attributes [:cn :member]
                :scope :base}) '()))

(defn get-user-memberships [conn user-dn]
  (or (ldap/search conn user-dn
               {:filter "(objectClass=inetorgperson)"
                :attributes [:cn, :displayName, :uid, :objectClass, :memberOf]
                :scope :base}) '()))

(defstate conn :start (get-conn (:ldap config)))
