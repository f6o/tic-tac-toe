(ns sample-frontend-using-reagent.tutorial
    (:require
      [reagent.core :as r]))

(defn square [i board-info]
  (let [s ((board-info :board) i)
        is-x? (deref (board-info :is-x-next?))]
    [:button.square
     {:value i
      :on-click
      (fn [e]
        (reset! s (if is-x? "X" "O"))
        (reset! (board-info :is-x-next?)
                (not is-x?)))}
     @s]))

(defn next-player [board-info]
  [:div.status
   "Next Player:"
   [:span.name
    (if (deref (board-info :is-x-next?)) "X" "O")]])

(defn board []
  (let [board-info
        {:board (vec (map #(r/atom "") (range 9)))
         :is-x-next? (r/atom true)}]
    [:div
     [next-player board-info]
     (for [r (range 0 9 3)]
       ^{:key (str "row" r)}
       [:div.row
        [square r board-info]
        [square (+ r 1) board-info]
        [square (+ r 2) board-info]])]))

(defn game []
  [:div.game
   [:div.game-board
    [board]]
   [:div.gameinfo
    [:div "status"]
    [:ol
     ^{:key 1} [:li "todo"]]]])

;; TODO: show the winner of a game
;; TODO: store a history
;; TODO: showing moves
