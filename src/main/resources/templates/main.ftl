<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
  <div>
    <@l.logout/>
    <span><a href="/user">User list</a></span>
  </div>
  <div>
    <form method="post" enctype="multipart/form-data">
      <label>
        <input type="text" name="text" placeholder="Сообщение" />
        <input type="file" name="file">
      </label>
      <input type="hidden" name="_csrf" value="${_csrf.token}" />
      <button type="submit">Добавить</button>
    </form>
  </div>
  <div>Список сообщений</div>
  <form method="get" action="/main">
    <label>
      <input type="text" name="filter" value="${filter!}">
    </label>
    <button type="submit">Найти</button>
  </form>
    <#list messages as message>
      <div>
        <b>${message.id}</b>
        <span>${message.text}</span>
        <strong>${message.user.username}</strong>
        <div>
            <#if message.filename??>
              <img src="/img/${message.filename}" alt="No picture :(">
            </#if>
        </div>
        <form method="get" action="/delete/${message.id}">
          <button type="submit">Удалить</button>
        </form>
      </div>
    <#else>
      No message
    </#list>
</@c.page>