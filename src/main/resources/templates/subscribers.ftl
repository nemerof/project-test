<#import "parts/common.ftl" as c>

<@c.page>
  List of Subscribers

  <#list subscribers as user>
    ${user.username}
  </#list>

</@c.page>