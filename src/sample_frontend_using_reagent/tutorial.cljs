(ns sample-frontend-using-reagent.tutorial
    (:require
      [reagent.core :as r]))

(defn square [i state]
  (let [s (state i)]
    [:button.square
     {:value i
      :on-click #(reset! s "X")}
     @s]))

(defn board []
  (let [state (vec (map #(r/atom "") (range 9)))
        status (r/atom "Next Player: X")]
    [:div
     [:div.status @status]
     (for [r (range 0 9 3)]
       ^{:key (str "row" r)}
       [:div.row
        [square r state]
        [square (+ r 1) state]
        [square (+ r 2) state]])]))

(defn game []
  [:div.game
   [:div.game-board
    [board]]
   [:div.gameinfo
    [:div "status"]
    [:ol
     ^{:key 1} [:li "todo"]]]])
