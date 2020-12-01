<#import "parts/common.ftl" as c>
<#import "parts/profilePicture.ftl" as p>

<@c.page>
  List of Subscribers:

  <#list subscribers as user>
    <h5 class="card-title">
        <@p.profilePicture user.profilePic 50 50/>
      <a href="/profile/${user.id}">${user.username}</a>
    </h5>
  </#list>

</@c.page>