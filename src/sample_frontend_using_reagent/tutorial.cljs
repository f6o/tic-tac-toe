(ns sample-frontend-using-reagent.tutorial
    (:require
      [reagent.core :as r]))

(defn board-filled? [board]
  (every? #(re-matches #"[OX]" (deref %)) board))

(defn won? [board]
  (some
   (fn [line]
     (every? (fn [i]
               (and
                (not (clojure.string/blank? (deref (board i))))
                (= (deref (board i))
                   (deref (board (first line))))))
             (rest line)))
   '((0 1 2) (3 4 5) (6 7 8)
     (0 3 6) (1 4 7) (2 5 8)
     (0 4 8) (2 4 6))))

(defn click-square [s board-info]
  (let [is-x? (deref (board-info :is-x-next?))
        result (deref (board-info :result))]
    (js/console.log (clj->js board-info))
    (when (and (clojure.string/blank? @s)
               (= result :in-play))
      (reset! s (if is-x? "X" "O"))
      (reset! (board-info :is-x-next?) (not is-x?))
      (reset!
       (board-info :result)
       (cond
         (won? (board-info :board)) :done
         (board-filled? (board-info :board)) :draw
         :else :in-play)))))

(defn square [i board-info]
  (let [s ((board-info :board) i)]
    [:button.square
     {:value i
      :on-click #(click-square s board-info)}
     @s]))

(defn empty-board []
  (vec (map #(r/atom "") (range 9))))

(defn draw-board []
  (vec
   (map #(r/atom %)
        (list
         "O" "X" "O"
         "O" "X" "X"
         "X" "O" ""))))

(defn create-new-board []
  {:board (empty-board)
   :result (r/atom :in-play)
   :is-x-next? (r/atom true)})

(defn show-status [board-info]
  [:div.status
   (case (deref (board-info :result))
     :done (str "Winner:"
                (if (deref (board-info :is-x-next?)) "O" "X"))
     :draw "Draw Game"
     (str "Next Player: " (if (deref (board-info :is-x-next?)) "X" "O")))])

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
   [:div.game-board [board]]
   [:div.gameinfo
    [:div "status"]
    [:ol
     ^{:key 1} [:li "todo"]]]])

;; TODO: store a history
;; TODO: showing moves
