(ns sample-frontend-using-reagent.tutorial
    (:require
      [reagent.core :as r]))

(defn board-filled? [board]
  (every? #(re-matches #"[OX]" %) board))

(defn won? [board]
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

(defn update-board-result [board-info]
  (swap!
   board-info assoc :result
   (cond
     (won? (@board-info :board)) :done
     (board-filled? (@board-info :board)) :draw
     :else :in-play)))

(defn click-square [i board-info board-history]
  (let [is-x? (@board-info :is-x-next?)
        current-board (@board-info :board)]
    (when (and (clojure.string/blank? ((@board-info :board) i))
               (= (@board-info :result) :in-play))
      (swap! board-history conj current-board)
      (swap! board-info assoc :board
             (assoc current-board i (if is-x? "X" "O")))
      (swap! board-info assoc :is-x-next? (not is-x?))
      (update-board-result board-info))))

(defn square [i board-info board-history]
  [:button.square
   {:value i
    :on-click #(click-square i board-info board-history)}
   ((@board-info :board) i)])

(defn create-new-board []
  (r/atom
   {:board (vec (repeat 9 ""))
    :result :in-play
    :is-x-next? true}))

(defn show-status [board-info]
  [:div.status
   (case (@board-info :result)
     :done (str "Winner:"
                (if (@board-info :is-x-next?) "O" "X"))
     :draw "Draw Game"
     :in-play (str "Next Player: "
                   (if (@board-info :is-x-next?) "X" "O")))])

(defn update-board-info [current-board-info new-layout]
  (swap! current-board-info assoc :board new-layout)
  (swap! current-board-info assoc
         :is-x-next?
          (= (count (filter #(= "X" %) new-layout))
             (count (filter #(= "O" %) new-layout))))
  (update-board-result current-board-info))

(defn game [board-info board-history]
  [:div.game
   [:div.game-board
    [show-status board-info]
    (for [r (range 0 9 3)]
      ^{:key (str "row" r)}
      [:div.board-row
       (for [c (range r (+ r 3))]
         ^{:key (str "cell" c)}
         [square c board-info board-history])])]
   [:div.game-info
    [:h4 "History"]
    [:ol.history
     (for [[i h] (map-indexed vector @board-history)]
       ^{:key i}
       [:li
        [:a
         {:href "javascript:void(0);"
          :on-click (fn [e]
                      (reset! board-history (subvec @board-history 0 i))
                      (update-board-info board-info h))}
         (if (= i 0)
           "Game Start"
           (str "Move #" i))]])]]])
