<#import "parts/common.ftl" as c>

<@c.page>
  <div class="form-group mt-3">
    <form method="post" enctype="multipart/form-data" action="/profile/edit/${userId}">
      <div class="form-group">
        <label>
          Username:
          <input type="text" name="username" value="${username}">
        </label>
      </div>
<#--      <div class="form-group">-->
<#--        <div class="custom-file">-->
<#--          <input type="file" name="file" id="customFile">-->
<#--          <label class="custom-file-label" for="customFile">Choose file</label>-->
<#--        </div>-->
<#--      </div>-->
      <input type="hidden" name="_csrf" value="${_csrf.token}" />
      <div class="form-group">
        <button type="submit" class="btn btn-primary">Добавить</button>
      </div>
    </form>
  </div>
</@c.page>