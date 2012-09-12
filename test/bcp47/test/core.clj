(ns bcp47.test.core 
  (:use clojure.test
        [bcp47.core :only (parse-bcp47)]))

; Data ------------------------------------------------------------------------
(def empty-registry
"File-Date: 2012-09-12
")

(def single-item-registry
"File-Date: 2012-09-12
%%
Type: language
Subtag: aa
Description: Afar
Added: 2005-10-16
")

(def two-item-registry
"File-Date: 2012-09-12
%%
Type: language
Subtag: aa
Description: Afar
Added: 2005-10-16
%%
Type: language
Subtag: ab
Description: Abkhazian
Added: 2005-10-16
Suppress-Script: Cyrl
")

(def long-line-registry
"File-Date: 2012-09-12
%%
Type: language
Subtag: pny
Description: Pinyin
Added: 2009-07-29
Comments: a Niger-Congo language spoken in Cameroon; not to be confused
  with the Pinyin romanization systems used for Chinese and Tibetan
")

(def multi-description-registry 
"File-Date: 2012-09-12
%%
Type: language
Subtag: aa
Description: Afar
Description: Multi
  Line
  Description
Added: 2005-10-16
")

(def all-fields-registry
"File-Date: 2012-09-12
%%
Type: language
Subtag: aa
Description: Afar
Added: 2005-10-16
Deprecated: 2009-01-01
Tag: zh-Hans
Prefix: rm
Preferred-Value: gan
Comments: Hello
Macrolanguage: zh
Suppress-Script: Cyrl
Scope: collection
")

; Convenience Functions -------------------------------------------------------
(defn date [y m d]
  {"year" y
   "month" m
   "day" d})

(defn dated [subtags]
  {"file-date" (date 2012 9 12)
   "subtags" subtags})


; Test Cases ------------------------------------------------------------------
(deftest test-empty
  (testing "An empty registry."
    (let [reg (parse-bcp47 empty-registry)]
      (is (= reg
             (dated []))))))

(deftest test-single-item
  (testing "A single basic item."
    (let [reg (parse-bcp47 single-item-registry)]
      (is (= reg
             (dated [{"type" "language"
                      "subtag" "aa"
                      "description" ["Afar"]
                      "added" (date 2005 10 16)}]))))))
(deftest test-two-items
  (testing "Two basic items."
    (let [reg (parse-bcp47 two-item-registry)]
      (is (= reg
             (dated [{"type" "language"
                      "subtag" "aa"
                      "description" ["Afar"]
                      "added" (date 2005 10 16)}
                     {"type" "language"
                      "subtag" "ab"
                      "description" ["Abkhazian"]
                      "added" (date 2005 10 16)
                      "suppress-script" "Cyrl"}]))))))
(deftest test-long-lines
  (testing "An item with fields containing long lines."
    (let [reg (parse-bcp47 long-line-registry)]
      (is (= reg
             (dated [{"type" "language"
                      "subtag" "pny"
                      "description" ["Pinyin"]
                      "added" (date 2009 7 29)
                      "comments" "a Niger-Congo language spoken in Cameroon; not to be confused with the Pinyin romanization systems used for Chinese and Tibetan"
                      }]))))))
(deftest test-multi-description
  (testing "An item with multiple description fields."
    (let [reg (parse-bcp47 multi-description-registry)]
      (is (= reg
             (dated [{"type" "language"
                      "subtag" "aa"
                      "description" ["Afar" "Multi Line Description"]
                      "added" (date 2005 10 16) }]))))))
(deftest test-all-fields
  (testing "An item with all possible fields."
    (let [reg (parse-bcp47 all-fields-registry)]
      (is (= reg
             (dated [{"type" "language"
                      "subtag" "aa"
                      "description" ["Afar"]
                      "added" (date 2005 10 16)
                      "deprecated" (date 2009 1 1)
                      "tag" "zh-Hans"
                      "prefix" "rm"
                      "preferred-value" "gan"
                      "comments" "Hello"
                      "macrolanguage" "zh"
                      "suppress-script" "Cyrl"
                      "scope" "collection"}]))))))

