(ns sample-frontend-using-reagent.prod
  (:require
    [sample-frontend-using-reagent.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
