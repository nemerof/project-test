<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<#import "parts/profilePicture.ftl" as p>
<#import "parts/pager.ftl" as pgr>

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
  <style>
      .hidden{
          display : none;
      }
  </style>
    <@pgr.pager url messages/>
    <#list messages.content as message>
        <#if !(isCurrentUser)><a href="/repost/${message.id}">add repost</a></#if>
      <div class="container mt-3">
        <div class="card m-auto" style="width: 600px">
          <div class="row mx-4 mt-3 md-1">
            <div class="col-11">
              <h5 class="card-title">
                  <@p.profilePicture message.user.profilePic 50 50/>
                <a href="/profile/${message.user.id}">${message.user.username}</a>
                  ${formatDateTime(message.postTime, 'MMM-dd-YYYY HH:mm')}
              </h5>
            </div>
            <div class="col-1" style="width:500px; float:right; text-align:left">
                <#if isAdmin || isCurrentUser>
                  <a href="/delete/${message.id}" style="text-decoration: none; font-size: 30px"><i class="fas fa-trash"></i></a>
                </#if>
            </div>
          </div>
          <div class="row mr-4 ml-5 mt-0 md-5"><p class="card-text"><span>${message.text}</span></p></div>
            <#if message.filename??>
              <img src="/img/${message.filename}" class="rounded mx-auto mt-4 md-1" alt="No pic :(" width="540" height="260">
            </#if>

          <div class="mx-3 my-1" style="font-size: 20px"><a href="/messages/${message.id}/like" style="text-decoration: none;">
                  <#if message.meLiked>
                    <i class="fas fa-heart"></i>
                  <#else>
                    <i class="far fa-heart"></i>
                  </#if>
                  ${message.likes}
            </a>
            <button class="btn btn-secondary" onclick="showHide(${message.id})" type="button"><i class="fas fa-comments"></i> ${message.comments?size}</button>
            <script>
                function showHide(messNumber) {
                    var tx = "addInfo"+messNumber;
                    var div = document.getElementById(tx);
                    div.classList.toggle('hidden');
                }
            </script>
          </div>
        </div>
        <div>
          <div class="hidden" id="addInfo${message.id}"><#list message.comments as comment>
              <div class="card m-auto" style="width: 500px">
                <div class="row ml-2 mt-1">
                  <a href="/profile/${comment.user.id}"><@p.profilePicture comment.user.profilePic 30 30/></a>
                  <a class="ml-1" href="/profile/${comment.user.id}">${comment.user.username}</a>
                </div>
                <div class="row ml-4">
                  <a>${comment.text}</a>
                </div>
                <div class="row ml-4">
                    <#if comment.filename??><@p.profilePicture comment.filename 270 130/></#if>
                </div>
              </div>
              </#list>
            <form enctype="multipart/form-data" method="post" action="/comment/${message.id}" class="m-auto"
                  style="width: 500px">
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
              <input type="hidden" name="_csrf" value="${_csrf.token}"/>
              <div class="form-group">
                <button type="submit" class="btn btn-primary">Save comment</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </#list>
</@c.page>
