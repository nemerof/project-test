<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
  <div class="row">
    <div class="card ml-5" style="width: 202px;">
      <img src="/static/images/default-profile-icon.png" class="rounded" alt="No pic :(" width="200" height="200">
    </div>
    <div class="card" style="height: 200px; width: 600px; margin-left: 21px;">
      <h5 class="card-title">${profileName}</h5>
    </div>
  </div>
  <div class="row">
    <form class="ml-5" style="width: 202px; ">
      <div class="custom-file">
        <input type="file" class="custom-file-input" id="customFile">
        <label class="custom-file-label mt-3" for="customFile">Change picture</label>
      </div>
    </form>
    <div class="card border-0" style="height: 200px; width: 600px; margin-left: 21px;">
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
  </div>
<#--  <div class="card mx-auto mt-3"  style="width: 202px;">-->
    <#list messages as message>
      <div class="card mt-5 mb-3 mx-auto" style="width: 600px">
        <h5 class="card-title">${message.user.username}</h5>
        <p class="card-text"><span>${message.text}</span></p>
          <#if message.filename??>
            <img src="/img/${message.filename}" class="rounded" alt="No pic :(" width="540" height="260">
          </#if>
      </div>
<#--  </div>-->
<#--      <form method="get" action="/delete/${message.id}">-->
<#--        <button type="submit">Удалить</button>-->
<#--      </form>-->
    </#list>
</@c.page>