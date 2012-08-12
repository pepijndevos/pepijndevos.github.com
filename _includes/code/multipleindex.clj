user=> (def index [[], []])
#'user/index
user=> (def index1 {1 0, 5 1})
#'user/index1
user=> (def index2 {"pepijn" 0, "ben" 1})
#'user/index2
user=> (def new-index (update-in index [(get index2 "pepijn")] conj "foo"))
#'user/new-index
user=> (get new-index (get index1 1))
["foo"]

