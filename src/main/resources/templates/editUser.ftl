<#import "parts/common.ftl" as c>

<@c.page>
  <div class="form-group mt-3">
    <div class="card ml-5" style="width: 202px;">
      <#if profilePic??>
          <#if !(profilePic == "default-profile-icon.png")>
            <img src="/img/${profilePic}" class="rounded" alt="No pic :(" width="200" height="200">
          <#else>
            <img src="/static/images/default-profile-icon.png" class="rounded" alt="No pic :(" width="200" height="200">
          </#if>
      </#if>
    </div>
    <form method="post" enctype="multipart/form-data" action="/edit">
      <div class="form-group">
        <label>
          Username:
          <input type="text" name="username" value="${username}">
        </label>
      </div>
      <div class="form-group">
        <div class="custom-file">
          <input type="file" name="profilePic" id="customFile"
            <#if profilePic??>${profilePic}</#if>
          >
          <label class="custom-file-label" for="customFile">Choose file</label>
        </div>
      </div>
      <input type="hidden" name="_csrf" value="${_csrf.token}" />
      <div class="form-group">
        <button type="submit" class="btn btn-primary">Добавить</button>
      </div>
    </form>
  </div>
</@c.page>