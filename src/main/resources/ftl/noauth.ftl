<#assign title="Account Information"/>
<!DOCTYPE html>
<html lang="en">
<head>
<#include "inc/head.ftl">
</head>

<body>

<#assign index="account"/>
<#include "inc/navbar.ftl">

<div class="container-fluid">
    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <h1>No permission</h1>


            <p>You (${email}) don't have permission to access this resource.  To get permission you
               should contact the administrator, <a href="mailto:${rootUser}">${rootUser}</a></p>
            <p>If you've logged in with the wrong email address then logout with the button below
               and try again with the correct address.</p>
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
</body>
</html>
