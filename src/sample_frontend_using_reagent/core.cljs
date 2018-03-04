(ns sample-frontend-using-reagent.core
  (:require
   [sample-frontend-using-reagent.tutorial :as tut]))

(defn mount-root []
  (reagent.core/render
   [tut/game (tut/create-new-state) (reagent.core/atom [])]
   (.getElementById js/document "app")))

(defn init! []
  (mount-root))
