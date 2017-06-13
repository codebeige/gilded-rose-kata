(ns gilded-rose.core-spec
  (:require [speclj.core :refer :all]
            [gilded-rose.core :as gr]))

(defn item [attrs]
  (merge {:name "Elixir of the Mongoose" :sell-in 99 :quality 33} attrs))

(defn update-item [attrs]
  (-> (gr/update-quality [(item attrs)])
      first
      (select-keys (keys attrs))))

(describe "gilded rose"

  (describe "item"
    (it "defines name, sell-in date, and quality"
      (should= {:name "My Item" :sell-in 2 :quality 30}
               (gr/item "My Item" 2 30))))

  (describe "update-quality"

    (it "degrades sell-in date and quality"
      (should= {:sell-in 2 :quality 29}
               (update-item {:sell-in 3 :quality 30})))

    (it "degrades quality faster after sell-in date has passed"
      (should= {:sell-in -1 :quality 28}
               (update-item {:sell-in 0 :quality 30})))

    (it "does not lower quality beyond 0"
      (should= {:quality 0}
               (update-item {:quality 0})))

    (it "always increases quality of aged brie"
      (should= {:name "Aged Brie" :quality 11}
               (update-item {:name "Aged Brie" :quality 10})))

    (it "does not increase quality above 50"
      (should= {:name "Aged Brie" :quality 50}
               (update-item {:name "Aged Brie" :quality 50})))

    (it "does not touch Sulfuras"
      (should= {:name "Sulfuras, Hand of Ragnaros" :sell-in 0 :quality 80}
               (update-item {:name "Sulfuras, Hand of Ragnaros"
                             :sell-in 0
                             :quality 80})))

    (it "increases quality of backstage passes"
      (should= {:name "Backstage passes to a TAFKAL80ETC concert"
                :sell-in 49
                :quality 21}
               (update-item {:name "Backstage passes to a TAFKAL80ETC concert"
                             :sell-in 50
                             :quality 20})))

    (it "increases quality of backstage passes by 2 when less than 10 days left"
      (should= {:name "Backstage passes to a TAFKAL80ETC concert"
                :sell-in 9
                :quality 22}
               (update-item {:name "Backstage passes to a TAFKAL80ETC concert"
                             :sell-in 10
                             :quality 20})))

    (it "increases quality of backstage passes by 3 when less than 5 days left"
      (should= {:name "Backstage passes to a TAFKAL80ETC concert"
                :sell-in 4
                :quality 23}
               (update-item {:name "Backstage passes to a TAFKAL80ETC concert"
                             :sell-in 5
                             :quality 20})))

    (it "drops quality of backstage passes to 0 after sell-in date"
      (should= {:name "Backstage passes to a TAFKAL80ETC concert"
                :sell-in -1
                :quality 0}
               (update-item {:name "Backstage passes to a TAFKAL80ETC concert"
                             :sell-in 0
                             :quality 20})))))
