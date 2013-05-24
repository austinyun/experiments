(ns crypto.base64-test
  (:require [clojure.test :refer :all]
            [crypto.base64 :refer [encode decode]]))

(def rfc4648-test-data
  '(("" "")
    ("f" "Zg==")
    ("fo" "Zm8=")
    ("foo" "Zm9v")
    ("foob" "Zm9vYg==")
    ("fooba" "Zm9vYmE=")
    ("foobar" "Zm9vYmFy")
    ))

(deftest base64-encoding
  (testing "Base64 encoding works"
    (doseq [[plain encoded] rfc4648-test-data]
      (is (=
           (encode (.getBytes plain))
           encoded)))))

(deftest base64-decoding
  (testing "Base64 decoding works"
    (doseq [[plain encoded] rfc4648-test-data]
      (is (=
           (decode (.getBytes encoded)))
           plain))))

(deftest invalid-decode-string
  (testing "Decode throws when input length isn't a multiple of 4"
    (is (thrown? AssertionError (decode "abc")))))

(deftest odd-chars
  (testing "Odd characters work"
    (is (=
         "!@#$%^&*()_+"
         (-> "!@#$%^&*()_+" .getBytes encode .getBytes decode)))))