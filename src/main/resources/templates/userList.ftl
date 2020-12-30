<#import "parts/common.ftl" as c>
<#import "parts/profilePicture.ftl" as p>
<#include "parts/security.ftl">

<@c.page>

  <div class="card border-0 mx-auto" style="height: 38px; width: 600px; margin-left: 21px;">
    <form method="get" action="/user" class="form-inline">
      <label>
        <input type="text" name="userFilter" class="form-control" placeholder="Search for user" style="width: 515px;"/>
      </label>
      <button type="submit" class="btn btn-primary ml-2">Search</button>
    </form>
  </div>
  <#list users.content as user>
    <div class="card my-3 mx-auto" style="height: 110px; width: 600px; margin-left: 21px;">
      <div class="row my-2">
        <div class="card ml-4" style="width: 92px;">
            <@p.profilePicture user.profilePic 90 90/>
        </div>
        <h5 class="card-title mx-3">
          <a href="/profile/${user.id}">${user.username}</a>
        </h5>
        <#if isAdmin && userId != user.id>
          <a href="/user/${user.id}">Edit</a>
          <a class="ml-1" href="/user/delete/${user.id}">Delete</a>
        </#if>
      </div>
    </div>
  </#list>
</@c.page>