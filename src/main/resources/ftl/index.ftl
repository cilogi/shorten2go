<#assign title="Mobile Friendly URL Shortener"/>
<!DOCTYPE html>
<html lang="en">
<head>
<#include "inc/head.ftl">
</head>

<body>
<#include "inc/spinner.ftl">
<div class="container-fluid">
<#assign index="active"/>
<#include "inc/navbar.ftl">

    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <h1>Mobile-friendly URL shortener</h1>

            <p class="lead">Use this shortener if you need short URLs which you expect people to type into a mobile
                phone, as the URL contains only lower-case letters.</p>

            <div role="form">
                <div class="form-group">
                    <label for="input">Type URL here</label>
                    <input class="form-control" id="input" name="input" type="text">
                </div>
                <div class="checkbox">
                    <label>
                      <input id="multiple" type="checkbox"> Allow multiple short URLs for this URL
                    </label>
                  </div>
            </div>
            <div id="answer-group" style="display:none; margin-top: 40px;">
                <hr/>
                <h4>Your short URL</h4>

                <p class="lead" id="answer"></p>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>

</div>
<!-- /.container -->

<#include "inc/foot.ftl">

<script src="/scripts/shorten.js"></script>

</body>
</html>
