(ns prefix-tree-cljc.core
  (:require [prefix-tree-cljc.core :as p]
            [clojure.test :refer [deftest is testing run-tests]]))

(def routes [{:get "/authorizations"}
             {:get "/authorizations/:id"}
             {:post "/authorizations"}
             {:delete "/authorizations/:id"}
             {:get "/applications/:client_id/tokens/:access_token"}
             {:delete "/applications/:client_id/tokens"}
             {:delete "/applications/:client_id/tokens/:access_token"}

             ;; Activity
             {:get "/events"}
             {:get "/repos/:owner/:repo/events"}
             {:get "/networks/:owner/:repo/events"}
             {:get "/orgs/:org/events"}
             {:get "/users/:user/received_events"}
             {:get "/users/:user/received_events/public"}
             {:get "/users/:user/events"}
             {:get "/users/:user/events/public"}
             {:get "/users/:user/events/orgs/:org"}
             {:get "/feeds"}
             {:get "/notifications"}
             {:get "/repos/:owner/:repo/notifications"}
             {:put "/notifications"}
             {:put "/repos/:owner/:repo/notifications"}
             {:get "/notifications/threads/:id"}
             {:get "/notifications/threads/:id/subscription"}
             {:put "/notifications/threads/:id/subscription"}
             {:delete "/notifications/threads/:id/subscription"}
             {:get "/repos/:owner/:repo/stargazers"}
             {:get "/users/:user/starred"}
             {:get "/user/starred"}
             {:get "/user/starred/:owner/:repo"}
             {:put "/user/starred/:owner/:repo"}
             {:delete "/user/starred/:owner/:repo"}
             {:get "/repos/:owner/:repo/subscribers"}
             {:get "/users/:user/subscriptions"}
             {:get "/user/subscriptions"}
             {:get "/repos/:owner/:repo/subscription"}
             {:put "/repos/:owner/:repo/subscription"}
             {:delete "/repos/:owner/:repo/subscription"}
             {:get "/user/subscriptions/:owner/:repo"}
             {:put "/user/subscriptions/:owner/:repo"}
             {:delete "/user/subscriptions/:owner/:repo"}

             ;; Gists
             {:get "/users/:user/gists"}
             {:get "/gists"}
             {:get "/gists/:id"}
             {:post "/gists"}
             {:put "/gists/:id/star"}
             {:delete "/gists/:id/star"}
             {:get "/gists/:id/star"}
             {:post "/gists/:id/forks"}
             {:delete "/gists/:id"}

             ;; Git Data
             {:get "/repos/:owner/:repo/git/blobs/:sha"}
             {:post "/repos/:owner/:repo/git/blobs"}
             {:get "/repos/:owner/:repo/git/commits/:sha"}
             {:post "/repos/:owner/:repo/git/commits"}
             {:get "/repos/:owner/:repo/git/refs"}
             {:post "/repos/:owner/:repo/git/refs"}
             {:get "/repos/:owner/:repo/git/tags/:sha"}
             {:post "/repos/:owner/:repo/git/tags"}
             {:get "/repos/:owner/:repo/git/trees/:sha"}
             {:post "/repos/:owner/:repo/git/trees"}

             ;; Issues
             {:get "/issues"}
             {:get "/user/issues"}
             {:get "/orgs/:org/issues"}
             {:get "/repos/:owner/:repo/issues"}
             {:get "/repos/:owner/:repo/issues/:number"}
             {:post "/repos/:owner/:repo/issues"}
             {:get "/repos/:owner/:repo/assignees"}
             {:get "/repos/:owner/:repo/assignees/:assignee"}
             {:get "/repos/:owner/:repo/issues/:number/comments"}
             {:post "/repos/:owner/:repo/issues/:number/comments"}
             {:get "/repos/:owner/:repo/issues/:number/events"}
             {:get "/repos/:owner/:repo/labels"}
             {:get "/repos/:owner/:repo/labels/:name"}
             {:post "/repos/:owner/:repo/labels"}
             {:delete "/repos/:owner/:repo/labels/:name"}
             {:get "/repos/:owner/:repo/issues/:number/labels"}
             {:post "/repos/:owner/:repo/issues/:number/labels"}
             {:delete "/repos/:owner/:repo/issues/:number/labels/:name"}
             {:put "/repos/:owner/:repo/issues/:number/labels"}
             {:delete "/repos/:owner/:repo/issues/:number/labels"}
             {:get "/repos/:owner/:repo/milestones/:number/labels"}
             {:get "/repos/:owner/:repo/milestones"}
             {:get "/repos/:owner/:repo/milestones/:number"}
             {:post "/repos/:owner/:repo/milestones"}
             {:delete "/repos/:owner/:repo/milestones/:number"}

             ;; Miscellaneous
             {:get "/emojis"}
             {:get "/gitignore/templates"}
             {:get "/gitignore/templates/:name"}
             {:post "/markdown"}
             {:post "/markdown/raw"}
             {:get "/meta"}
             {:get "/rate_limit"}

             ;; Organizations
             {:get "/users/:user/orgs"}
             {:get "/user/orgs"}
             {:get "/orgs/:org"}
             {:get "/orgs/:org/members"}])
