(ns sample-frontend-using-reagent.tutorial
    (:require
      [reagent.core :as r]))

(defn board-filled? [board]
  (every? #(re-matches #"[OX]" %) board))

(defn culculate-winner [board]
  (some
   (fn [line]
     (every? (fn [i]
               (and
                (not (clojure.string/blank? (board i)))
                (= (board i) (board (first line)))))
             (rest line)))
   '((0 1 2) (3 4 5) (6 7 8)
     (0 3 6) (1 4 7) (2 5 8)
     (0 4 8) (2 4 6))))

(defn update-board-result [state]
  (swap!
   state assoc :result
   (cond
     (culculate-winner (@state :squares)) :done
     (board-filled? (@state :squares)) :draw
     :else :in-play)))

(defn click-square [i state board-history]
  (let [is-x? (@state :is-x-next?)
        current-board (@state :squares)]
    (when (and (clojure.string/blank? ((@state :squares) i))
               (= (@state :result) :in-play))
      (swap! board-history conj current-board)
      (swap! state assoc :squares
             (assoc current-board i (if is-x? "X" "O")))
      (swap! state assoc :is-x-next? (not is-x?))
      (update-board-result state))))

(defn square [i state board-history]
  [:button.square
   {:value i
    :on-click #(click-square i state board-history)}
   ((@state :squares) i)])

(defn create-new-state []
  (r/atom
   {:squares (vec (repeat 9 ""))
    :result :in-play
    :is-x-next? true}))

(defn show-status [state]
  [:div.status
   (case (@state :result)
     :done (str "Winner:"
                (if (@state :is-x-next?) "O" "X"))
     :draw "Draw Game"
     :in-play (str "Next Player: "
                   (if (@state :is-x-next?) "X" "O")))])

(defn update-state [current-state new-layout]
  (swap! current-state assoc :squares new-layout)
  (swap! current-state assoc
         :is-x-next?
          (= (count (filter #(= "X" %) new-layout))
             (count (filter #(= "O" %) new-layout))))
  (update-board-result current-state))

(defn game [state board-history]
  [:div.game
   [:div.game-board
    [show-status state]
    (for [r (range 0 9 3)]
      ^{:key (str "row" r)}
      [:div.board-row
       (for [c (range r (+ r 3))]
         ^{:key (str "cell" c)}
         [square c state board-history])])]
   [:div.game-info
    [:h4 "History"]
    [:ol.history
     (for [[i h] (map-indexed vector @board-history)]
       ^{:key i}
       [:li
        [:button
         {:on-click (fn [e]
                      (reset! board-history (subvec @board-history 0 i))
                      (update-state state h))}
         (str "Go to "
              (if (= i 0)
                "game start"
                (str "move #" i)))]])]]])
