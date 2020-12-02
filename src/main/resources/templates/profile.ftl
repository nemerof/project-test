<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<#import "parts/profilePicture.ftl" as p>
<#import "parts/messageView.ftl" as m>

<@c.page>
  <div class="row">
    <div class="card ml-5" style="width: 202px;">
        <@p.profilePicture curProfPic 200 200/>
    </div>
    <div class="card" style="height: 200px; width: 600px; margin-left: 21px;">
      <h5 class="card-title mx-3 my-2">${profileName}
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

    <div class="card border-0 my-3" style="height: 242px; width: 600px; margin-left: 21px;">
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
  <div class="ml-6 mt-2" style="margin-left: 50px">
      <#if !isCurrentUser>
          <#if !isSubscriber>
            <a class="btn btn-primary" href="/profile/subscribe/${profileId}">Subscribe</a>
          <#else>
            <a class="btn btn-primary" href="/profile/unsubscribe/${profileId}">Unsubscribe</a>
          </#if>
      </#if>
  </div>
<#--  <div class="card mx-auto mt-3"  style="width: 202px;">-->
    <@m.mess userId isAdmin/>
</@c.page>
