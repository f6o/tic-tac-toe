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

(defn update-board-status [board-info new-info]
  (reset!
   (board-info :result)
   (cond
     (won? (new-info :board)) :done
     (board-filled? (new-info :board)) :draw
     :else :in-play)))

(defn click-square [s board-info board-history]
  (let [is-x? (deref (board-info :is-x-next?))
        result (deref (board-info :result))]
    (when (and (clojure.string/blank? @s)
               (= result :in-play))
      (swap! board-history
             conj
             (vec (map #(deref %) (board-info :board))))
      (reset! s (if is-x? "X" "O"))
      (reset! (board-info :is-x-next?) (not is-x?))
      (update-board-status board-info board-info))))

(defn square [i board-info board-history]
  (let [s ((board-info :board) i)]
    [:button.square
     {:value i
      :on-click #(click-square s board-info board-history)}
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

(defn update-board-info [current-board-info new-layout]
  (doseq [[o n] (map list
                     (current-board-info :board)
                     new-layout)]
    (reset! o n))
  (reset! (current-board-info :is-x-next?)
          (=  (count (filter #(= "X" %) new-layout))
              (count (filter #(= "O" %) new-layout))))
  (update-board-status current-board-info current-board-info))

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

;; TODO: showing moves
