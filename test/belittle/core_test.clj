(ns belittle.core-test
  (:require [clojure.test :refer :all]
            [belittle.core :refer :all]
            [belittle.other-namspace :refer :all :as on]))

(def incer inc)

(defn get-decer
  [x y]
  {(m decer x) y})

(def get-decer-0-1
  {(m decer 0) 1})

(deftest mock-tests
  (testing "A basic mock"
    (given
     {(incer 0) 2}
     (is
      (= (incer 0) 2))))
  (testing "Call limits report error"
    (given
     {(incer 0) (once 2)}
     (is
      (= (incer 0) 2))
     (is
      (= (incer 0) nil))))
  (testing "Completion checked"
    (given
     {(incer 0) (once 2)}))
  (testing "Merge multiple maps"
    (given (merge {(incer 0) 2}
                  {(decer 0) 1})
           (is (= (incer 0)
                  2)
               (= (decer 0)
                  1))))
  (testing "Function returned mocks"
    (given (merge {(incer 0) 2}
                  (get-decer 0 1))
           (is (= (incer 0)
                  2)
               (= (decer 0)
                  1))))
  (testing "Static mocks"
    (given (merge {(incer 0) 2}
                  get-decer-0-1)
           (is (= (incer 0)
                  2)
               (= (decer 0)
                  1)))))

(mock-tests)

(deftest mock-other-namespace-fn
  (testing "Unmocked"
    (is
     (= (decer 1)
        0)))
  (testing "Fully qualified"
    (given
     {(belittle.other-namspace/decer 1) 2}
     (is
      (= (belittle.other-namspace/decer 1)
         2))))
  (testing "Short hand qualified"
    (given
     {(on/decer 1) 2}
     (is
      (= (on/decer 1)
         2))))
  (testing "Refer'ed"
    (given
     {(decer 1) 2}
     (is
      (= (decer 1)
         2)))))

(mock-other-namespace-fn)

(defn equals
  [e]
  (fn [a]
    (= e a)))

(deftest is-threading
  (is-> 1
        (equals 1)))

(is-threading)