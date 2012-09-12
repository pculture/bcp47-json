bcp47-json
==========

JSON versions of the [IANA BCP47 Language Subtag Registry][reg].  Copy them and
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

The file format looks like this:

    {"file-date": <date>,
     "subtags": [<subtag>, <subtag>, ...]}

A date is encoded like so:

    {"year": 2012, "month": 9, "day": 12}

Each subtag corresponds to an entry in the original registry.  It can have some
or all of the fields:

    {"type": <string>,
     "subtag": <string>,
     "added": <date>,
     "deprecated": <date>,
     "tag": <string>,
     "prefix": <string>,
     "preferred-value": <string>,
     "comments": <string>,
     "macrolanguage": <string>,
     "supress-script": <string>,
     "scope": <string>,
     "description": [<string>, <string>, ...]}

As you can see, most values are passed straight through as plain strings from
the IANA registry.  There are three exceptions:

`added` and `deprecated` are encoded as dates (see above).

`description` is an array of strings.  In the IANA registry some tags have more
than one description, for some reason I probably don't want to know.  If there's
just one description for a tag it will be encoded as an array of one element.

### Generate a Fresh `bcp47.json` from the Registry

The `bcp47.json` file in the repo is generated whenever we get around to it.  If
you want a fresh, up-to-date version you'll need to run the Clojure code that
creates it.

You'll need [Leiningen 2][lein].  Then do the following:

    git clone git://github.com/pculture/bcp47-json.git
    cd bcp47-json

    mkdir checkouts
    cd checkouts
    git clone git://github.com/youngnh/parsatron.git
    cd ..

    lein run

Sorry about the Parsatron checkout, but you need to run from the latest master
because of bugs.  Once there's another release of it I'll fix that.

That will overwrite the contents of `bcp47.json` with the latest data pulled
from the IANA registry.

[lein]: https://github.com/technomancy/leiningen

License
-------

Copyright (c) 2012 [Participatory Culture Foundation][pcf] and contributors.

MIT/X11 Licensed.

[pcf]: http://pculture.org/
