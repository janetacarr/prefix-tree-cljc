(ns prefix-tree-cljc.core
  (:require [clojure.string :as string]))

;; /user/:id/profile
;; /users

(defn ->prefixtree
  "Creates a prefix (radix) tree root"
  ([data prefix]
   (->prefixtree data prefix false))
  ([data prefix is-param]
   {:children prefix
    :data data}))

(defn- ->prefix
  "Returns a map representing an 'edge' in the graph."
  [prefix prefixtree]
  {prefix prefixtree})

(defn- found?
  [elements-found s]
  (= elements-found (count s)))

(defn search
  "Searches `prefixtree`, radix tree, created with `->prefixtree`, for
  string `s`. Optionally takes `elements-found` for recursion.
  I only recommend you use the 2-arity version"
  ([prefixtree s]
   (search prefixtree s 0))
  ([prefixtree s elements-found]
   (if (= elements-found (count s))
     (:data prefixtree)
     ;; TODO: Maybe pull some of this out into functions?
     (if (and (some? (:children prefixtree))
              (< elements-found (count s)))
       (let [prefixes (-> (:children prefixtree)
                          (keys))
             suffix (subs s elements-found)]
         (loop [next-nodes (->> prefixes
                                (filterv #(string/starts-with? suffix %)))]
           (let [next-node-k (peek next-nodes)
                 next-node (get (:children prefixtree) next-node-k)]
             (if-not next-node
               ;; TODO: pluggable coercion ???
               ;; con
               (let [edges (:children prefixtree)
                     pf (peek (filterv #(string/starts-with? % "/:") (keys edges)))
                     next-node (get edges pf)
                     param (-> (subs s (inc elements-found))
                               (string/split #"/")
                               (get 0))]
                 (if next-node
                   (search next-node s (+ elements-found (inc (count param))))
                   (recur (pop next-nodes))))
               (search next-node s (+ elements-found (count next-node-k)))))))))))

(def testtree
  (->prefixtree
   {:get (fn [req] {:status 200
                    :body "root"})}
   (->prefix "/user"
             (->prefixtree {:get (fn [req] {:status 200 :body "user"})}
                           (->prefix "s"
                                     (->prefixtree
                                      {:get (fn [req] {:status 200
                                                       :body "users"})}
                                      (->prefix "/:user-id"
                                                (->prefixtree
                                                 {:get (fn [req] {:status 200
                                                                  :body "user-id"})}
                                                 (->prefix "/settings"
                                                           (->prefixtree
                                                            {:get (fn [req] {:status 200
                                                                             :body "settings"})}
                                                            nil))))))))))

;; 7531902468
;;((:get (search testtree "/users/7531")) {})
;;((:get (search testtree "/users")) {})
;;((:get (search testtree "/users/asotehusatnoheusnathoeusnthoe/settings")) {})
;;((:get (search testtree "/user")) {})


;; consider (take-while (fn [xs] (apply = xs))))
(defn longest-prefix
  [& strs]
  (->> strs
       (apply mapv (fn [& cs]
                     (when (apply = cs)
                       (first cs))))
       (reduce (fn [acc c]
                 (if (nil? c)
                   (reduced acc)
                   (conj acc c))) [])
       (string/join)))

(defn remove-prefix
  [s prefix]
  (string/join (subvec (vec s) (count prefix))))

;;(remove-prefix "owlers" "ow")

;; (subvec (vec "owlers") (count "ow"))
(defn insert
  ([prefixtree s data]
   (insert prefixtree s data 0))
  ([prefixtree s data elements-found]
   (assert (string? s) "insert must be string")
   (when (< elements-found (count s))
     (let [prefixes (-> (:children prefixtree)
                        (keys))
           suffix (subs s elements-found)
           next-nodes (->> prefixes
                           (filterv #(string/starts-with? suffix %)))]
       (if (empty? next-nodes)
         (let [existing-prefix (->> prefixes
                                    (mapv (fn [prefix]
                                            [prefix (longest-prefix suffix prefix)]))
                                    (filterv #(-> % (get 1) not-empty))
                                    (peek))
               [prefix lcp] existing-prefix]
           (if (empty? existing-prefix)
             (merge prefixtree {:children (merge (:children prefixtree)
                                                 (->prefix suffix
                                                           (->prefixtree data
                                                                         nil)))})
             ;; TODO: cleanup this monstrosity
             (merge prefixtree
                    {:children (merge
                                (-> (:children prefixtree)
                                    (dissoc prefix))
                                (->prefix
                                 lcp
                                 (->prefixtree
                                  nil
                                  (merge
                                   (->prefix
                                    (remove-prefix prefix lcp)
                                    (->prefixtree
                                     (-> prefixtree :children (get prefix) :data)
                                     (-> prefixtree :children (get prefix) :children)))
                                   (->prefix
                                    (remove-prefix suffix lcp)
                                    (->prefixtree data nil))))))})))
         (reduce (fn [prefixtree next-node-k]
                   (let [next-node (get (:children prefixtree) next-node-k)]
                     (merge prefixtree
                            {:children
                             (assoc (:children prefixtree)
                                    next-node-k
                                    (insert next-node
                                            s
                                            data
                                            (+ elements-found (count next-node-k))))})))
                 prefixtree
                 next-nodes))))))


(def long-tree (-> testtree
                   (insert "/owners" {:get (fn [req] {:status 200 :body "owners"})})))
(def owlers-tree (insert long-tree "/owlers" {:get (fn [req] {:status 200 :body "owlers"})}))

(def owners-tree (insert owlers-tree "/users/:user-id/owner" {:get (fn [req] {:status 200 :body "id/owner"})}))


;; (clojure.pprint/pprint owners-tree)

;; ((:get (search owners-tree "/owners")) {})
;; (search long-tree "/7531")
