<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
  <div class="mb-1 ml-2" style="font-size: 20px">Registration</div>
<#--    ${message}-->
    <@l.login "/registration" true/>
</@c.page>