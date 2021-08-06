(ns fwpd.core
  (:gen-class))
(require '(clojure string))

(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

; (defn glitter-filter
;   "Return list of names"
;   [minimum-glitter records]
;   (map
;     #(get % :name)
;     (filter #(>= (:glitter-index %) minimum-glitter) records))
;     )

(defn mapped-suspects
  "Return list of vampire suspects from CSV file"
  []
  (mapify (parse (slurp filename))))

(defn has-name?
  "Does set contain :name property?"
  [properties]
  (println "[has-name?] properties:" properties)
  (get properties :name))

(defn has-glitter?
  "Does set contain :glitter-index property?"
  [properties]
  (println "[has-glitter?] properties:" properties)
  (get properties :glitter-index))

(defn validate
  "Validate records for :name and :glitter-index properties"
  [keywords, record]
  (println "keywords:" keywords)
  (println "record:" record)
  ; (map #(contains? record %) keywords))
  (and (has-name? record) (has-glitter? record)))
  ; (map (keys #(println "key: " %) keywords)))

(defn append
  "Append new suspect to list of vampire suspects"
  [suspect suspects]
  (println "suspect:" suspect)
  (println "suspects:" suspects)
  (if (validate vamp-keys suspect)
    (conj suspects suspect)
    suspects
    ; (println "Not added")
  ))

(defn -main
  "PROGRAM MAIN"
  []
  (append {:names "foo"} mapped-suspects)
  (append {:name "Nikki" :glitter-index "15"} mapped-suspects)
  )
  ; (if (append {:names "foo"} mapped-suspects)
  ;   (println "Appended")
  ;   (println "Not valid")
  ;   ))
  ; (println (append {:name "Chip Castle" :glitter-index 20} (mapped-suspects)))
  ; (println (mapped-suspects))

(-main)
