(ns sample-frontend-using-reagent.tutorial
    (:require
      [reagent.core :as r]))

(defn square [n state]
  (let [s (state n)]
    [:button.square
     {:value n
      :on-click #(reset! s "X")}
     @s]))

(defn board-row [start state]
  [:div.board-row
   [square start state]
   [square (+ start 1) state]
   [square (+ start 2) state]])

(defn board []
  (let [state (vec (map #(r/atom "") (range 9)))
        status (r/atom "Next Player: X")]
    [:div
     [:div.status @status]
     [board-row 0 state]
     [board-row 3 state]
     [board-row 6 state]]))

(defn game []
  [:div.game
   [:div.game-board [board]]
   [:div.gameinfo
    [:div "status"]
    [:ol
     ^{:key 1} [:li "todo"]]]
   ])
