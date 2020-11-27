<#import "parts/common.ftl" as c>

<@c.page>
  List of Subscriptions
  <#list subscriptions as user>
      ${user.username}
  </#list>
</@c.page>