(ns troy-west.thimble.talk
  (:require [troy-west.arche :as arche])
  (:import (java.util Date)))

;; TODO: would be better in an externalised HugCQL file (see: arche)

(def statements {::select "SELECT * FROM talk WHERE id = :id"
                 ::insert "INSERT INTO talk (id, rating, date) VALUES (:id, :rating, :date)"})

(defn appraisal
  [talk rating]
  {:id     talk
   :rating (int rating)
   :date   (Date.)})

(defn insert
  ([conn appraisal]
   (arche/execute conn ::insert {:values appraisal}))
  ([conn talk rating]
   (insert conn (appraisal talk rating))))

(defn select
  [conn id]
  (arche/execute conn ::select {:values {:id id}}))
