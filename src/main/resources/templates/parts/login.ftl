<#include "security.ftl">

<#macro login path isRegisterForm>
  <style>
    p {
      color: red;
    }
  </style>
  <form action="${path}" method="post">
    <div class="form-group row">
      <label class="col-sm-2 col-form-label">User Name :</label>
      <div class="col-sm-6">
        <label>
          <input type="text" name="username" class="form-control" placeholder="User name" />
        </label>
        <#if usernameError??>
          <p>${usernameError}</p>
        </#if>
      </div>
    </div>
    <div class="form-group row">
      <label class="col-sm-2 col-form-label">Password:</label>
      <div class="col-sm-6">
        <label>
          <input type="password" name="password" class="form-control" placeholder="Password" />
        </label>
        <#if passwordError??>
          <p>${passwordError}</p>
        </#if>
      </div>
    </div>
    <div class="form-group row">
      <label class="col-sm-2 col-form-label">E-Mail:</label>
      <div class="col-sm-6">
        <label>
          <input type="email" name="email" class="form-control" placeholder="E-Mail" />
        </label>
        <#if emailError??>
          <p>${emailError}</p>
        </#if>
        <#if emailExists??>
          <p>${emailExists}</p>
        </#if>
        <#if usernameExists??>
          <p>${usernameExists}</p>
        </#if>
      </div>
    </div>

    <input type="hidden" name="_csrf" value="${_csrf.token}" />
      <#if !isRegisterForm><a href="/registration">Add new user</a></#if>
    <button class="btn btn-primary" type="submit"><#if isRegisterForm>Create<#else>Sign In</#if></button>
  </form>
</#macro>

<#macro logout>
  <form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit">
      <#if isAuthorized>
        Sign Out
      <#else>
        Sign In
      </#if>
    </button>
  </form>
</#macro>