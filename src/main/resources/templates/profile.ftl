<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
  <div class="row">
    <div class="card ml-5" style="width: 202px;">
      <img src="/static/images/default-profile-icon.png" class="rounded" alt="No pic :(" width="200" height="200">
    </div>
    <div class="card" style="height: 200px; width: 600px; margin-left: 21px;">
      <h5 class="card-title">${profileName}
          <#if userId == profileId>
            <a href="/edit">Edit</a>
          </#if>
      </h5>
      <div class="container">
        <div class="row">
          <div class="col">
            <div class="card">
              <div class="card-body" style="background-color: lightblue">
                <div class="card-title">Subscribers</div>
                <h4 class="card-text"><a href="/profile/subscribers/${profileId}">${subscribers}</a></h4>
              </div>
            </div>
          </div>
          <div class="col">
            <div class="card">
              <div class="card-body" style="background-color: lightblue">
                <div class="card-title">Subscriptions</div>
                <h4 class="card-text"><a href="/profile/subscriptions/${profileId}">${subscriptions}</a></h4>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <#if userId == profileId>
  <div class="row">

    <form class="ml-5" style="width: 202px; ">
      <div class="custom-file">
        <input type="file" class="custom-file-input" id="customFile">
        <label class="custom-file-label mt-3" for="customFile">Change picture</label>
      </div>
    </form>

    <div class="card border-0" style="height: 200px; width: 600px; margin-left: 21px;">
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
  </div>
  </#if>
  <script type="text/javascript">
      $(document).on('change', '.custom-file-input', function (event) {
          $(this).next('.custom-file-label').html(event.target.files[0].name);

      });
  </script>
<#if !isCurrentUser>
    <#if !isSubscriber>
      <a class="btn btn-primary" href="/profile/subscribe/${profileId}">Subscribe</a>
    <#else>
      <a class="btn btn-primary" href="/profile/unsubscribe/${profileId}">Unsubscribe</a>
    </#if>
</#if>
<#--  <div class="card mx-auto mt-3"  style="width: 202px;">-->
    <#list messages as message>
      <div class="card mt-5 mb-3 mx-auto" style="width: 600px">
        <h5 class="card-title">${message.user.username}</h5>
        <p class="card-text"><span>${message.text}</span></p>
          <#if message.filename??>
            <img src="/img/${message.filename}" class="rounded" alt="No pic :(" width="540" height="260">
          </#if>
      </div>
<#--  </div>-->
<#--      <form method="get" action="/delete/${message.id}">-->
<#--        <button type="submit">Удалить</button>-->
<#--      </form>-->
    </#list>
</@c.page>
