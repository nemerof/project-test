<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<#--  <a class="btn btn-primary mb-3 mx-auto" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">-->
<#--    Add new Message-->
<#--  </a>-->
<#--  <div class="collapse" id="collapseExample">-->
  <div class="card mt-3 mb-3 mx-auto border-0" style="width: 600px;">
      <#--<div class="card w-50 mb-3 mx-auto border-0">-->
    <div class="form-group mt-3">
      <form method="post" enctype="multipart/form-data">
        <div class="form-group">
          <label for="comment">Comment:</label>
          <textarea class="form-control" rows="3" id="comment" name="text"></textarea>
        </div>
        <div class="form-group">
          <div class="custom-file">
            <input type="file" name="file" id="customFile">
            <label class="custom-file-label" for="customFile">Choose file</label>
          </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <div class="form-group">
          <button type="submit" class="btn btn-primary">Добавить</button>
        </div>
      </form>
    </div>
  </div>
  <script type="text/javascript">
      $(document).on('change', '.custom-file-input', function (event) {
          $(this).next('.custom-file-label').html(event.target.files[0].name);

      });
  </script>
<#--  </div>-->
    <#list messages as message>
      <div class="card m-auto" style="width: 600px">
        <h5 class="card-title mx-4 my-3"><a href="/profile/${message.user.id}">${message.user.username}</a></h5>
        <p class="card-text mx-4 my-3"><span>${message.text}</span></p>
          <#if message.filename??>
            <img src="/img/${message.filename}" class="rounded mx-auto my-3" alt="No pic :(" width="540" height="260">
          </#if>
      </div>
      <br>
<#--        <form method="get" action="/delete/${message.id}">-->
<#--          <button type="submit">Удалить</button>-->
<#--        </form>-->
    </#list>
</@c.page>