(ns ^:figwheel-no-load sample-frontend-using-reagent.dev
  (:require
    [sample-frontend-using-reagent.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
