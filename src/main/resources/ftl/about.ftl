<#assign title="Account Information"/>
<!DOCTYPE html>
<html lang="en">
<head>
<#include "inc/head.ftl">
</head>

<body>

<#include "inc/navbar.ftl">

<div class="container-fluid">
    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <h1>Features</h1>
            <ul>
                <li>You can use the shortener in your organization, at low cost, if you
                    own a short domain.</li>
                <li>The implementation is built on App Engine and designed to scale up.</li>
                <li>The short URLs generate are mobile-friendly, only using the 26 lower case
                    Latin letters, which makes URLs easier to type on a mobile phone.</li>
                <li>You can have several short URLs for each long URL.  This is useful if
                    a short URL is placed in more than one location, and you want to
                    know where the hits were. This can be more convenient than adding query
                    parameters to your URLs to indicate location.</li>
                <li>Using your own shortener means that your data is private to you and
                    won't be sold to other people.</li>
            </ul>

            <h1>Using the Shortener</h1>

            <p>There are three modes of use.  You can  get a shortened URL interactively
            <a href="/index.html">here</a>, or (if you are authorized) get a custom URL
            <a href="/custom.html">here</a> or use the API endpoints to get a JSON document containing
            a short or custom URL.</p>

            <h2>The API endpoints</h2>

            <p>The URLs for the API endpoints are <code>/secure/api/shorten</code> and <code>/secure/api/custom</code>,
               the first for generating a short URL and the second for creating a custom link where the user
               specifies -- for example <code>http://glas.gy/museums</code>.</p>  Both endpoints are accessed using the
               <code>GET</code> HTTP method with parameters setting the input arguments.</p>
            <h4><code>/secure/api/shorten</code></h4>
            <p>The shorten endpoint takes the following parameters</p>
            <ul>
                <li><em>uuid</em> The user <em>uuid</em> which is shown on the <a href="/secure/account">account</a>
                page, when you're logged in.  This should be kept secret and not shown to any unauthorized person.</li>
                <li><em>url</em> The URL to be shortened.</li>
                <li><em>multiple</em> If present, with any value, multiple short URLs for the same target URL are
                    allowed</li>
            </ul>
            <p>A typical query might look like:</p>
            <pre>
http://shorten2go.appspot.com/api?uuid=abcd-efgh-0123-4567&url=http://cilogi.com
            </pre>
            <p>The return from this endpoint is a JSON document (with mime type <code>application/json</code>)
               which typically looks like:</p>
            <pre>
{"status": "ok", "url" : "http://cilogi.com", "shortUrl" : "http://glas.gy/guides"}
            </pre>
            <p>The JSON object returned has the following fields:</p>
            <ul>
                <li><em>status</em> Either <code>"ok"</code> or <code>"fail"</code>.</li>
                <li><em>url</em> If status is <code>ok</code>, this is the long URL to be shortened.</li>
                <li><em>shortUrl</em> If status is <code>ok</code> this is the short URL</li>
                <li><em>error</em> If status is <code>fail</code> this is the reason for the failure.</li>
            </ul>
            <h4><code>/secure/api/custom</code></h4>
            <p>The custom endpoint takes the following parameters</p>
            <ul>
                <li><em>url</em> The URL for which a custom link is required.</li>
                <li><em>custom</em> The path of the custom URL.  The path excludes the protocol and host parts
                    of the URL.  So, for example, to get <code>http://glas.gy/museums</code> as the
                    custom URL this field would be <code>museums</code>.</li>
            </ul>
            <p>The JSON object returned has the same format as the <code>shorten</code> endpoint.</p>

            <h2>Code</h2>

            <p>The code is available on <a href="https://github.com/cilogi/shorten2go">Github</a>.
               Use the Github project as a starting
               point for your own code and as the place to register bugs, as well as additional features
               you may want.</p>

            <h1>Warning</h1>
            <p>The website is intended only to demonstrate the code in the Github repository.  Any
               short links created will, at some point, be destroyed and this may well be without warning.
               Any changes to the code may also render old links unusable and may occur at any time.</p>
            <p>The site runs on the free tier of App Engine and service will be interrupted if the site runs out of
               resources.</p>
            <p>You have been warned!</p>
        </div>
        <div class="col-md-2"></div>
    </div>
    <!-- row -->

</div>
<!-- /.container -->

<#include "inc/foot.ftl">

<script src="/scripts/shorten.js"></script>
</body>
</html>
