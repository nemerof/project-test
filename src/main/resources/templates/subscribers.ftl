<#import "parts/common.ftl" as c>

<@c.page>
  List of Subscribers:

  <#list subscribers as user>
    <p>${user.username}</p>
  </#list>

</@c.page>