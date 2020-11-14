<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
  <div>
      <@l.logout />
  </div>
  <div>
    <form method="post">
      <label>
        <input type="text" name="text" placeholder="Сообщение" />
      </label>
      <input type="hidden" name="_csrf" value="${_csrf.token}" />
      <button type="submit">Добавить</button>
    </form>
  </div>
  <div>Список сообщений</div>
  <form method="get" action="/filter">
    <label>
      <input type="text" name="filter">
    </label>
    <button type="submit">Найти</button>
  </form>
    <#list messages as message>
      <div>
        <b>${message.id}</b>
        <span>${message.text}</span>
        <strong>${message.user.username}</strong>
      </div>
    <#else>
      No message
    </#list>
</@c.page>