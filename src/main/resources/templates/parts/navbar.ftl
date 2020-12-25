<#include "security.ftl">
<#import "login.ftl" as l>
<#import "profilePicture.ftl" as p>

<nav class="navbar navbar-expand-lg navbar-light" style="background-color: #e3f2fd;">
  <a class="navbar-brand" href="/">Communication</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item">
        <a class="nav-link" href="/">Messages</a>
      </li>
        <li class="nav-item">
            <a class="nav-link" href="/user">User list</a>
        </li>
        <#if isAuthorized>
            <li class="nav-item ml-5">
              <form method="get" action="/" class="form-inline">
                <label>
                  <input type="text" name="filter" class="form-control" placeholder="Search for message"/>
                </label>
                <button type="submit" class="btn btn-primary ml-2">Search</button>
              </form>
            </li>
        </#if>
    </ul>
    <#if isAuthorized>
    <div class="dropdown">
      <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenu2"
              data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="background-color: #e3f2fd;">
          <#if isAuthorized>
          <div class="navbar-text">
            <a href="/profile/${userId}" id="navbarUsername">
                <#if username??>
                    <div>
                      ${username}
                      <@p.profilePicture profilePic 30 30/>
                    </div>
                <#else>missing</#if>
            </a>
          </div>
          <#else>
            LogIn first
          </#if>
      </button>
      <div class="dropdown-menu dropdown-menu-right">
        <a class="dropdown-item" href="/profile/${userId}">
              <#if username??>
                  ${username}
                  <#if profilePic??>
                      <@p.profilePicture profilePic 30 30/>
                  </#if>
              <#else>missing</#if>
        </a>
        <a class="dropdown-item" href="/edit">
          Edit profile
        </a>
        <div class="dropdown-divider"></div>
        <a class="dropdown-item justify-content-center">
            <@l.logout />
        </a>
      </div>
    </div>
        <#else>
          LogIn first
            <@l.logout />
    </#if>
  </div>
</nav>