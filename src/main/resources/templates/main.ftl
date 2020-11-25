<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<#--  <a class="btn btn-primary mb-3 mx-auto" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">-->
<#--    Add new Message-->
<#--  </a>-->
<#--  <div class="collapse" id="collapseExample">-->
    <#include "parts/addForm.ftl">
<#--  </div>-->
    <#list messages as message>
      <div class="card mt-3 mb-3 mx-auto" style="width: 600px">
        <h5 class="card-title">${message.user.username}</h5>
        <p class="card-text"><span>${message.text}</span></p>
            <#if message.filename??>
              <img src="/img/${message.filename}" class="rounded" alt="No pic :(" width="540" height="260">
            </#if>
        </div>
<#--        <form method="get" action="/delete/${message.id}">-->
<#--          <button type="submit">Удалить</button>-->
<#--        </form>-->
    </#list>
</@c.page>