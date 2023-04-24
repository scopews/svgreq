MAKEFLAGS += --jobs=4

lint:
	clj-kondo --lint src test

tests:
	node_modules/.bin/shadow-cljs compile test

outdated:
	clj -M:outdated && npm outdated
