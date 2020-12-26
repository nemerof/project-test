<#include "security.ftl">

<#macro login path isRegisterForm>
  <style>
    p {
      color: red;
    }
    .hidden{
        display : none;
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
        <p style="color: black" class="ml-2"><button class="btn btn-secondary" onclick="showHide()" type="button">Additional information: </button></p>
      <div id="addInfo" class="hidden">
        <div class="form-group row mx-0 md-4 mt-2">
          <label>Profile photo:</label>
          <div class="custom-file col-sm-6" style="width: 400px">
            <input class="custom-file-input" type="file" name="profilePicture" id="customFile" style="outline: none; box-shadow: none" onchange="changeText(this)"/>
            <label class="custom-file-label" for="customFile" id="chooseFile">Choose file</label>
            <br/>
          </div>
          <div class="col-sm-6">
            <img class="img-fluid mt-1" id="blah" src="/static/images/default-profile-icon.png" width="200px" height="200px"/>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-sm-2 col-form-label">Real name:</label>
          <div class="col-sm-6">
            <label>
              <input type="text" name="realName" class="form-control" placeholder="Real Name"/>
            </label>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-sm-2 col-form-label">Date of birth:</label>
          <div class="col-sm-6">
            <label>
              <input type="date" max="2020-10-10" name="dateOfBirth" class="form-control" placeholder="Date of birth"/>
            </label>
          </div>
        </div>
        <div class="form-group row">
          <label class="col-sm-2 col-form-label">City:</label>
          <div class="col-sm-6">
            <label>
              <input type="text" name="city" class="form-control" placeholder="City"/>
            </label>
          </div>
        </div>
      </div>
        <script>
            function showHide() {
                var div = document.getElementById("addInfo");
                div.classList.toggle('hidden');
            }
        </script>

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