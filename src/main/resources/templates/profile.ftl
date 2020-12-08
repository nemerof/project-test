<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<#import "parts/profilePicture.ftl" as p>
<#import "parts/messageView.ftl" as m>

<@c.page>
  <div class="row">
    <div class="card ml-5" style="width: 202px;">
        <@p.profilePicture curProfPic 200 200/>
    </div>
    <div class="card" style="height: 200px; width: 600px; margin-left: 21px">
      <h5 class="card-title mx-3 my-2">${profileName}</h5>
      <hr class="my-1">
      <p class="ml-2 my-1">Real name:
          <#if user.realName??>
            ${user.realName}
          </#if>
      </p>
      <p class="ml-2 my-1">Birthday:
          <#if user.dateOfBirth??>
              ${user.dateOfBirth}
          </#if>
      </p>
      <p class="ml-2 my-1">City:
          <#if user.city??>
              ${user.city}
          </#if>
      </p>
      <hr class="my-1">
      <div class="container">
        <div class="row">
          <div class="col">
            <div class="card border-0">
              <div class="card-body" style="height: 25px; padding-top: 5px">
                <h4 class="card-text">Subscribers: <a href="/profile/subscribers/${profileId}">${subscribers}</a></h4>
              </div>
            </div>
          </div>
          <div class="col">
            <div class="card border-0">
              <div class="card-body" style="height: 25px; padding-top: 5px">
                <h4 class="card-text">Subscriptions: <a href="/profile/subscriptions/${profileId}">${subscriptions}</a></h4>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <#if userId == profileId>
    <div class="card border-0 mt-3 mx-auto" style="height: 242px; width: 600px; margin-left: 21px;">
        <#--<div class="card w-50 mb-3 mx-auto border-0">-->
      <div class="form-group mt-3">
        <form enctype="multipart/form-data" method="post" action="/profile/${userId}">
          <div class="form-group">
            <label for="comment">Comment:</label>
            <textarea class="form-control" rows="3" id="comment" name="text"></textarea>
          </div>
          <div class="form-group">
            <div class="custom-file">
              <input class="custom-file-input" type="file" name="file" id="customFile">
              <label class="custom-file-label" for="customFile">Choose file</label>
            </div>
          </div>
          <input type="hidden" name="_csrf" value="${_csrf.token}" />
          <div class="form-group">
            <button type="submit" class="btn btn-primary">Save message</button>
          </div>
        </form>
      </div>
    </div>
  </#if>
  <script type="text/javascript">
      $(document).on('change', '.custom-file-input', function (event) {
          $(this).next('.custom-file-label').html(event.target.files[0].name);
      });
  </script>
  <div class="ml-6 mt-2" style="margin-left: 50px">
      <#if !isCurrentUser>
          <#if !isSubscriber>
            <a class="btn btn-primary" href="/profile/subscribe/${profileId}">Subscribe</a>
          <#else>
            <a class="btn btn-primary" href="/profile/unsubscribe/${profileId}">Unsubscribe</a>
          </#if>
      </#if>
  </div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

  <script type="text/javascript">
    $(document).ready(function () {

      if (localStorage.getItem("main.ftl") != null) {
        $(window).scrollTop(localStorage.getItem("main.ftl"));
      }

      $(window).on("scroll", function() {
        localStorage.setItem("main.ftl", $(window).scrollTop());
      });

    });
  </script>
<#--  <div class="card mx-auto mt-3"  style="width: 202px;">-->
    <@m.mess userId isAdmin/>
</@c.page>
