bcp47-json
==========

JSON versions of the [IANA BCP47 Language Subtag Registry](reg).  Copy them and
get on with your life instead of dealing with yet another custom text-based data
format.

This repo also contains a tool written in Clojure to convert the IANA registry
to JSON.

[reg]: https://www.iana.org/assignments/language-subtag-registry

Usage
-----

There are two main things you might want to use this repository for.

### A JSON Version of the BCP47 Subtag Registry

Copy the `bcp47.json` file and do whatever you want with it.

### Generate a Fresh `bcp47.json` from the Registry

The `bcp47.json` file in the repo is generated whenever we get around to it.  If
you want a fresh, up-to-date version you'll need to run the Clojure code that
creates it.

You'll need [Leiningen 2][lein].  Then do the following:

    git clone git://github.com/pculture/bcp47-json.git
    cd bcp47-json
    lein run

That will overwrite the contents of `bcp47.json` with the latest data pulled
from the IANA registry.

[lein]: https://github.com/technomancy/leiningen

License
-------

Copyright (c) 2012 Participatory Culture Foundation and contributors.

MIT/X11 Licensed.
