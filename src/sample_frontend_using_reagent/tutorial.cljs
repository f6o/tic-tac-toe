(ns sample-frontend-using-reagent.tutorial
    (:require
      [reagent.core :as r]))

(defn square [n state]
  [:button.square
   {:value n
    :on-click #(reset! state "X")}
   @state])

(defn new-state []
  (r/atom ""))

(defn board-row [start]
  [:div.board-row
   [square start (new-state)]
   [square (+ start 1) (new-state)]
   [square (+ start 2) (new-state)]])

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
