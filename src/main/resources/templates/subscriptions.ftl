<#import "parts/common.ftl" as c>

<@c.page>
  List of Subscriptions:

  <#list subscriptions as user>
    <p>${user.username}</p>
  </#list>

</@c.page>