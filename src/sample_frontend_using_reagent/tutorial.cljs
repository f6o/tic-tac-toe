(ns sample-frontend-using-reagent.tutorial
    (:require
      [reagent.core :as r]))

(defn board-filled? [board]
  (every? #(re-matches #"[OX]" (deref %)) board))

(defn decide-board-winner [board]
  (some (fn [line]
          (every? (fn [i]
                      (and
                       (not (clojure.string/blank? (deref (board i))))
                       (= (deref (board i))
                          (deref (board (first line))))))
                  (rest line)))
        '((0 1 2)
          (3 4 5)
          (6 7 8)
          (0 3 6)
          (1 4 7)
          (2 5 8)
          (0 4 8)
          (2 4 6))))

(defn square [i board-info]
  (let [s ((board-info :board) i)
        is-x? (deref (board-info :is-x-next?))]
    [:button.square
     {:value i
      :on-click
      (fn [e]
        (when (not (board-filled? (board-info :board)))
          (if (not (deref (board-info :done)))
            (reset! s (if is-x? "X" "O")))
          (if (or
               (deref (board-info :done))
               (decide-board-winner (board-info :board)))
            (reset! (board-info :done) true)
            (reset! (board-info :is-x-next?) (not is-x?)))))}
     @s]))

(defn empty-board []
  (vec (map #(r/atom "") (range 9))))

(defn draw-board []
  (vec
   (map
    #(r/atom %)
    (list
     "O" "X" "O"
     "O" "X" "X"
     "X" "O" ""))))

(defn create-new-board []
  {:board (empty-board)
   :done (r/atom false)
   :is-x-next? (r/atom true)})

(defn show-player [board-info]
  (if (deref (board-info :is-x-next?)) "X" "O"))

(defn show-status [board-info]
  [:div.status
   (cond (board-filled? (board-info :board)) "Draw Game"
         (deref (board-info :done)) (str "Winner:" (show-player board-info))
         :else (str  "Next Player:" (show-player board-info)))])

(defn board []
  (let [board-info (create-new-board)]
    [:div
     [show-status board-info]
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

;; TODO: store a history
;; TODO: showing moves
