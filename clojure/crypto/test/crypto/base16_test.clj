(ns crypto.base16-test
  (:require [clojure.test :refer :all]
            [crypto.base16 :refer :all]))

(def rfc4648-test-data
  '(("" "")
    ("f" "66")
    ("fo" "666f")
    ("foo" "666f6f")
    ("foob" "666f6f62")
    ("fooba" "666f6f6261")
    ("foobar" "666f6f626172")
    ))

(deftest base16-encoding
  (testing "Base16 encoding works"
  (doseq [[plain encoded] rfc4648-test-data]
    (is (=
         (apply str (encode (.getBytes plain)))
         encoded)))))

(deftest base64-decoding
  (testing "Base64 decoding works"
    (doseq [[plain encoded] rfc4648-test-data]
      (is (=
           (apply str (decode (.getBytes encoded)))
           plain)))))

(deftest base64-decoding'
  (testing "Base64 decoding works"
    (doseq [[plain encoded] rfc4648-test-data]
      (is (=
           (apply str (decode' (.getBytes encoded)))
           plain)))))

(deftest base64-decoding''
  (testing "Base64 decoding works"
    (doseq [[plain encoded] rfc4648-test-data]
      (is (=
           (apply str (decode'' (.getBytes encoded)))
           plain)))))

(deftest base64-decoding'''
  (testing "Base64 decoding works"
    (doseq [[plain encoded] rfc4648-test-data]
      (is (=
           (apply str (decode''' (.getBytes encoded)))
           plain)))))