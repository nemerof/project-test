<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <a class="navbar-brand" href="/">Sweater</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item">
        <a class="nav-link" href="/">Messages</a>
      </li>
        <#if isAdmin>
          <li class="nav-item">
            <a class="nav-link" href="/user">User list</a>
          </li>
        </#if>
        <#if isAuthorized>
            <li class="nav-item ml-5">
              <form method="get" action="/" class="form-inline">
                <label>
                  <input type="text" name="filter" class="form-control" placeholder="Search for message">
                </label>
                <button type="submit" class="btn btn-primary ml-2">Search</button>
              </form>
            </li>
        </#if>
    </ul>
    <#if isAuthorized>
    <div class="navbar-text mr-3">
      <a href="/profile/${userId}">${name}</a>
    </div>
    </#if>
      <@l.logout />
  </div>
</nav>