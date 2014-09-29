# Mobile-friendly URL Shortener

To use this application you need to own a suitable short domain.  We
are using `glas.gy` since we live in Glasgow.

There is a [demo here][5] which can be used to try the shortener
out. It runs on the free tier of App Engine so will run out of
resources if used heavily.

There are two reasons for creating our own shortening service.

First, there are circumstances where people have to type in short
URLs. In our case we are building [mobile guides][1] and short URLs
are one way for visitors to discover the guides.  Traditional short
URLs uses a combination of lower and upper-case letters and digitsL.
This is good for making sure that URLs are short, but it can be a pain
to type digits and capitals on a mobile device.  Hence our shortener
uses only the 26 lower-case characters `a-z`.

Second, when it comes to analytics, it can be useful for more than one
short URL map to a given long URL.  For instance, there may be
multiple physical locations which display the same URL and it may be
important to know which one was accessed. In this application, using
the REST API, its possible for multiple short URLs mapping to a single
long URL.

Using only the lower case latin characters is not as big a drawback as
it seems _if you own a domain_ as its likely that the number of URLs
you want to encode won't exceed the low millions.  You can get 300
million URLs into 6 lower case characters, and its much easier to type
6 lower-case characters than 4 mixed characters on a mobile.

There are three interfaces.  The first is a public facing one, on the
`index.html` page which let's anyone generate a mobile-friendly short
URL.

The second provides a simple REST API for generating URLs, and this
requires a token, which is provided to anyone who logs in with a
Google ID.

The third interface allows you to create your own short URL by
specifying both the contraction and the original.  This is useful if
you want something like `glas.gy/pubs` where the URL has some meaning.

## Running your own

The implementation is written in Java and runs on Google App Engine.
A demo is available [here][2]. The demo uses App Engine's free tier,
so may not be available if heavy use is being made of it.

You can run you own shortener (but, of course, you'll need your own
short domain) by installing it from [Github][3]

## Bare bones

This is a bare-bones implementation. At the moment there are no
analytics capabilities.  The cost under App Engine would to be
high if the datastore is used. It is possible to use the
[Logs API][4] to record hits and analyse the logs periodically to
provide analytics information.  Logging is configured to make this a
feasible route.

## Short URL generator

The most interesting aspect of the shortener is the generation of the
short URLs when the implementation is distributed.  We have chosen to
create a generator which eventually iterates over all the long
integers.  The generation isn't in strict order (its not clear what
this would mean in a distributed system) but assuming a _fair_ random
number generator every integer should eventually be output.

The implementation uses a sharded generator, with a central
distribution entity which provides each shard with a range of
numbers.  The shards distribute the numbers and get replenished from
the central entity when they are exhausted.

## API

The API is a single HTTPS call

    https://shorten2go.appspot.com/api?url=<url to shorten>&uuid=<uuid>&multiple=<any>

The _url_ parameter is the URL which should be shortened. The _uuid_
parameter is your _uuid_. The _multiple_ parameter, if present, will
always generate a new shortening. Without the _multiple_ parameter an
existing shortening is returned (at random if there are several) if
one exists.

[1]: http://www.cilogi.com
[2]: http://shorten2go.appspot.com
[3]: http://github.com/cilogi/shorten2go
[4]: https://cloud.google.com/appengine/docs/java/logs/
[5]: https://shorten2go.appspot.com
