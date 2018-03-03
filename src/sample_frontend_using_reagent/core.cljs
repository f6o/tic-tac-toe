(ns sample-frontend-using-reagent.core
  (:require
   [sample-frontend-using-reagent.tutorial :as tut]
   [reagent.core :as r]))

;; -------------------------
;; Views

(def todo (r/atom (list "Cutting edges"
                        "Buying the milk"
                        "Cleaning seats")))

(defn del-link []
  [:a.del
   {:href "#" :on-click (fn [self] (print self))}
   "-"])

(defn todo-item [item]
  [:li ^{:key item}
   [:span.item item]
   [del-link]])

(defn todo-component []
  [:ul
   (for [item @todo]
     [todo-item item])])

(def global-counter (r/atom 0))

(defn count-up-link [counter]
  [:a.up
   {:href "#" :on-click (fn [] (swap! counter inc))}
   "add"])

(defn counter-component [counter]
  [:div
   "Now:" [:span.count @counter]
   [count-up-link counter]])

;; [:div
;;  [todo-component]
;;  [counter-component (r/atom 0)]]

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render
   [tut/game (tut/create-new-board) (r/atom [])]
   (.getElementById js/document "app")))

(defn init! []
  (mount-root))
