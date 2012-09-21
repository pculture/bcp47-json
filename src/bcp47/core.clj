(ns bcp47.core
  (:refer-clojure :exclude [char])
  (:use [the.parsatron]
        [clojure.string :only (lower-case)]
        [clojure.pprint :only (pprint)]
        [cheshire.core :only (generate-string)]))


; Data ------------------------------------------------------------------------
(def registry-url
  "https://www.iana.org/assignments/language-subtag-registry")

(defn get-raw-registry []
  (str (slurp registry-url) \newline))


; Parsing ---------------------------------------------------------------------
(defparser number [len]
  (let->> [year-chars (times len (digit))]
    (always (Integer/parseInt (apply str year-chars)))))

(defparser linebreak []
  (char \newline))

(defparser to-eol []
  (let->> [result (many (token (partial not= \newline)))]
    (always (apply str result))))


; Dates
(defparser date []
  (let->> [year (number 4)
           _ (char \-)
           month (number 2)
           _ (char \-)
           day (number 2)]
    (always {"year" year
             "month" month
             "day" day})))


; Lines
(defparser separator []
  (string "%%\n"))

(defparser labelled [label p]
  (attempt
    (let->> [_ (string (str label ": "))
             result p
             _ (linebreak)]
      (always [(lower-case label) result]))))

(defparser labelled-line [label]
  (labelled label (to-eol)))

(defparser labelled-date [label]
  (labelled label (date)))

(defparser bcp-line-continuation []
  (let->> [_ (string "  ")
           result (to-eol)
           _ (linebreak)]
    (always result)))

(defparser bcp-long-line []
  (let->> [initial-line (to-eol)
           _ (linebreak)
           extra-lines (many (bcp-line-continuation))]
    (always (apply str (interpose \space (concat [initial-line]
                                                 extra-lines))))))

(defparser labelled-long-line [label]
  (attempt
    (let->> [_ (string (str label ": "))
             result (bcp-long-line)]
      (always [(lower-case label) result]))))

(defparser bcp-type []
  (labelled-line "Type"))

(defparser bcp-subtag []
  (labelled-line "Subtag"))

(defparser bcp-added []
  (labelled-date "Added"))

(defparser bcp-deprecated []
  (labelled-date "Deprecated"))

(defparser bcp-suppress []
  (labelled-line "Suppress-Script"))

(defparser bcp-scope []
  (labelled-line "Scope"))

(defparser bcp-tag []
  (labelled-line "Tag"))

(defparser bcp-preferred []
  (labelled-line "Preferred-Value"))

(defparser bcp-prefix []
  (labelled-line "Prefix"))

(defparser bcp-comments []
  (labelled-long-line "Comments"))

(defparser bcp-description []
  (labelled-long-line "Description"))

(defparser bcp-macrolanguage []
  (labelled-line "Macrolanguage"))

(defparser line []
  (choice (bcp-type)
          (bcp-subtag)
          (bcp-description)
          (bcp-added)
          (bcp-deprecated)
          (bcp-suppress)
          (bcp-scope)
          (bcp-tag)
          (bcp-preferred)
          (bcp-prefix)
          (bcp-comments)
          (bcp-macrolanguage)))


; Data
(defn filter-multi-fields [data label]
  (map second (filter #(= (first %) label) data)))

(defn merge-data
  "Take a sequence of data line pairs and return a map."
  [data]
  (let [descriptions (filter-multi-fields data "description")
        prefixes (filter-multi-fields data "prefix")
        other (remove #(#{"description" "prefix"} (first %)) data)
        result (into {} other)
        result (if (seq descriptions)
                 (assoc result "description" (vec descriptions))
                 result)
        result (if (seq prefixes)
                 (assoc result "prefix" (vec prefixes))
                 result)]
    result))

(defparser entry []
  (let->> [_ (separator)
           data (many1 (line))]
    (always (merge-data data))))

(defparser bcp47 []
  (let->> [_ (string "File-Date: ")
           file-date (date)
           _ (linebreak)
           data (many (entry))]
    (always {"file-date" file-date "subtags" (vec data)})))

(defn parse-bcp47 [raw]
  (run (bcp47) raw))


; Main ------------------------------------------------------------------------
(defn -main [& args]
  (let [registry (parse-bcp47 (get-raw-registry))
        json (generate-string registry {:pretty true})]
    (spit "bcp47.json" json)))
