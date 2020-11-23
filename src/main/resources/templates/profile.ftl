<#import "parts/common.ftl" as c>

<@c.page>
    <#list messages as message>
      <div class="card w-50 mb-3 mx-auto">
        <h5 class="card-title">${message.user.username}</h5>
        <p class="card-text"><span>${message.text}</span></p>
          <#if message.filename??>
            <img src="/img/${message.filename}" class="rounded" alt="No pic :(" width="540" height="260">
          </#if>
      </div>
      <form method="get" action="/delete/${message.id}">
        <button type="submit">Удалить</button>
      </form>
    <#else>
      No message
    </#list>
</@c.page>