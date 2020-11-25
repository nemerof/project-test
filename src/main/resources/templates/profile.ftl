<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
  <div class="row">
    <div class="card ml-5"  style="width: 202px;">
      <img src="/static/images/default-profile-icon.png" class="rounded" alt="No pic :(" width="200" height="200">
    </div>
    <div class="card" style="height: 200px; width: 600px; margin-left: 21px;">
      <h5 class="card-title">${name}</h5>
    </div>
  </div>
  <div class="row">
    <form>
      <div class="custom-file">
        <input type="file" class="custom-file-input" id="customFile">
        <label class="custom-file-label ml-5 mt-3" for="customFile" style="width: 200px">Change picture</label>
      </div>
    </form>
    <#include "parts/addForm.ftl">
  </div>
<#--  <div class="card mx-auto mt-3"  style="width: 202px;">-->
    <#list messages as message>
      <div class="card mt-3 mb-3 mx-auto" style="width: 600px">
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