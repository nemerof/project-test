<#include "security.ftl">

<#macro login path isRegisterForm>
  <style>
    p {
      color: red;
    }
  </style>
  <form action="${path}" enctype="multipart/form-data"  method="post">
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
    <#if isRegisterForm>
      <div class="form-group row">
        <label class="col-sm-2 col-form-label">E-Mail:</label>
        <div class="col-sm-6">
          <label>
            <input type="email" name="email" class="form-control" placeholder="E-Mail"/>
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
      <div class="form-group mx-0 md-4 mt-2">
        <label>Profile photo:</label>
        <div class="custom-file" style="width: 400px">
          <input type="file" name="profilePicture" id="customFile" style="outline: none; box-shadow: none">
          <label class="custom-file-label" for="customFile">Choose file</label>
        </div>
      </div>
      <div class="form-group row">
        <label class="col-sm-2 col-form-label">Real name:</label>
        <div class="col-sm-6">
          <label>
            <input type="text" name="realName" class="form-control" placeholder="Real Name" />
          </label>
        </div>
      </div>
      <div class="form-group row">
        <label class="col-sm-2 col-form-label">Date of birth:</label>
        <div class="col-sm-6">
          <label>
            <input type="date" name="dateOfBirth" class="form-control" placeholder="Date of birth" />
          </label>
        </div>
      </div>
    </#if>
<#--      <#if isRegisterForm>-->
<#--        <div class="form-group row">-->
<#--          <label class="col-sm-2 col-form-label">Email:</label>-->
<#--          <div class="col-sm-6">-->
<#--            <input type="email" name="email" class="form-control" placeholder="some@some.com" />-->
<#--          </div>-->
<#--        </div>-->
<#--      </#if>-->
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <#if !isRegisterForm><a href="/registration">Add new user</a></#if>
    <button class="btn btn-primary" type="submit"><#if isRegisterForm>Create<#else>Sign In</#if></button>
      <#if errorUsernamePassword??>
        <a style="color: red">${errorUsernamePassword}</a>
      </#if>
  </form>
</#macro>

<#macro logout>
  <form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary ml-3 text-center" type="submit">
      <#if isAuthorized>
        Sign Out
      <#else>
        Sign In
      </#if>
    </button>
  </form>
</#macro>