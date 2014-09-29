<#assign title="Account Information"/>
<!DOCTYPE html>
<html lang="en">
<head>
<#include "inc/head.ftl">
</head>

<body>

<#assign account="active"/>
<#include "inc/navbar.ftl">

<div class="container-fluid">
    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <h1>Your account details</h1>

            <p><b>Name:</b> ${user.email}</p>

            <p><b>UUID:</b> ${user.uuid}</p>

            <p>You'll need the UUID in order to access the shortener via the RESTful API.</p>
            <div style="margin-top: 2em">
                <a href="${logoutUrl}" class="btn btn-primary btn-lg active" role="button">Logout</a>
            </div>
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
