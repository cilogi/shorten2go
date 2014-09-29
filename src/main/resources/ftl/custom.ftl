<#assign title="Mobile Friendly URL Shortener"/>
<!DOCTYPE html>
<html lang="en">
  <head>
      <#include "inc/head.ftl">
  </head>

  <body>
  <#include "inc/spinner.ftl">
  <div class="container-fluid">
      <#assign custom="active"/>
      <#include "inc/navbar.ftl">

      <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <h1>Mobile-friendly URL shortener</h1>
            <p class="lead">Create a custom short URL. Type only the path (ie to get <code>http://glas.gy/pubs</code>
               enter only <code>pubs</code>).  The custom URL must start with a letter or a digit.</p>
            <div role="form">
                <div class="form-group">
                    <label for="input">Type URL here</label>
                    <input id="input" class="form-control" name="input" type="text">
                </div>
                <div class="form-group">
                    <label for="custom">Type Custom URL here</label>
                    <input id="custom" class="form-control" name="custom" type="text">
                </div>
                <div style="margin-top: 2em">
                    <button id="create" class="btn btn-primary btn-lg active" role="button">Create Custom Short URL</button>
                </div>
                <div id="answer-group" style="display:none; margin-top: 40px;">
                    <hr/>
                    <p class="lead" id="answer"></p>
                </div>
            </div>
        </div>
        <div class="col-md-2"></div>
      </div>

    </div><!-- /.container -->

  <#include "inc/foot.ftl">

  <script src="/scripts/custom.js"></script>

  </body>
</html>
