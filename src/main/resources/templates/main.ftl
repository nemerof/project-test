<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<#import "parts/messageView.ftl" as m>
<#include "parts/security.ftl">

<@c.page>
<#--  <a class="btn btn-primary mb-3 mx-auto" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">-->
<#--    Add new Message-->
<#--  </a>-->
<#--  <div class="collapse" id="collapseExample">-->
  <div class="card border-0 my-3 mx-auto" style="height: 242px; width: 600px; margin-left: 21px;">
      <#--<div class="card w-50 mb-3 mx-auto border-0">-->
    <div class="form-group mt-3">
      <form enctype="multipart/form-data" method="post" action="/">
        <div class="form-group">
          <label for="comment">Comment:</label>
          <textarea class="form-control" rows="3" id="comment" name="text"></textarea>
        </div>
        <div class="form-group">
          <div class="custom-file">
            <input class="custom-file-input" type="file" name="file" id="customFile">
            <label class="custom-file-label" for="customFile">Choose file</label>
          </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <div class="form-group">
          <button type="submit" class="btn btn-primary">Save message</button>
        </div>
      </form>
    </div>
  </div>
  <script type="text/javascript">
      $(document).on('change', '.custom-file-input', function (event) {
          $(this).next('.custom-file-label').html(event.target.files[0].name);

      });
  </script>
    <@m.mess userId isAdmin/>
</@c.page>