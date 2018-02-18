(ns sample-frontend-using-reagent.tutorial
    (:require
      [reagent.core :as r]))

(defn square [n]
  [:button.square (str n)])

(defn board-row [start]
  [:div.board-row
   [square start]
   [square (+ start 1)]
   [square (+ start 2)]
   ])

(defn board []
  [:div
   [:div.status "status"]
   [board-row 0]
   [board-row 3]
   [board-row 6]]
  )

(defn game []
  [:div.game
   [:div.game-board [board]]
   [:div.gameinfo
    [:div "status"]
    [:ol
     ^{:key 1} [:li "todo"]]]
   ])