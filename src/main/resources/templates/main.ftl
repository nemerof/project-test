<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<#include "parts/security.ftl">
<#import "parts/profilePicture.ftl" as p>
<#import "parts/pager.ftl" as pgr>

<@c.page>
<#--  <a class="btn btn-primary mb-3 mx-auto" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">-->
<#--    Add new Message-->
<#--  </a>-->
<#--  <div class="collapse" id="collapseExample">-->
  <div class="card border-0 my-3 mx-auto" style="height: 242px; width: 600px; margin-left: 21px;">
      <#--<div class="card w-50 mb-3 mx-auto border-0">-->
    <div class="form-group mt-0">
      <form enctype="multipart/form-data" method="post" action="/">
        <div class="form-group">
          <label for="comment">Post:</label>
          <textarea class="form-control" rows="3" id="comment" name="text"></textarea>
        </div>
        <div class="form-group">
          <div class="custom-file">
            <input class="custom-file-input" type="file" name="file" id="customFile" onchange="changeText(this)"/>
            <label class="custom-file-label" for="customFile" id="chooseFile">Choose file</label>
          </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div class="form-group">
          <button type="submit" class="btn btn-primary">Save message</button>
        </div>
      </form>
    </div>
  </div>
  <script type="text/javascript">
      $(document).on('change', '.custom-file-input', function (event) {
          $(this).next('.custom-file-label').html(event.target.files[0].name);

      });
  </script>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

  <script type="text/javascript">
      $(document).ready(function () {

          if (localStorage.getItem("main.ftl") != null) {
              $(window).scrollTop(localStorage.getItem("main.ftl"));
          }

          $(window).on("scroll", function () {
              localStorage.setItem("main.ftl", $(window).scrollTop());
          });

      });
  </script>

  <style>
      .hidden {
          display: none;
      }
  </style>
    <@pgr.pager url messages/>
    <div id="messageList">
    <#list messages.content as message>
      <div class="container mt-3" id="messages">
        <div class="card m-auto" style="width: 600px">
          <div class="row mx-4 mt-3 md-1">
            <div class="col-10">
              <h5 class="card-title">
                  <@p.profilePicture message.user.profilePic 50 50/>
                <a href="/profile/${message.user.id}" id="messageUsername">${message.user.username}</a>
                  ${formatDateTime(message.postTime, 'MMM-dd-YYYY HH:mm')}
              </h5>
            </div>
            <div class="col-1">
                <#if !(loginUserId==message.user.id)>
                  <a href="/repost/${message.id}" style="text-decoration: none; font-size: 30px">
                    <i class="fas fa-share"></i>
                  </a>
                </#if>
            </div>
            <div class="col-1" style="width:500px; float:right; text-align:left">
                <#if isAdmin || loginUserId==message.user.id>
                  <a href="/delete/${message.id}" style="text-decoration: none; font-size: 30px">
                    <i class="fas fa-trash"></i>
                  </a>
                </#if>
            </div>
          </div>
          <div class="row mr-4 ml-5 mt-0 md-5" id="messageText">
            <p class="card-text">
              <span>${message.text}</span>
            </p>
          </div>
            <#if message.filename??>
              <img src="https://storage.googleapis.com/communication-network/${message.filename}" class="rounded mx-auto mt-4 md-1" alt="No pic :(" width="540"
                   height="260"/>
            </#if>

          <div class="mx-3 my-1" style="font-size: 20px"><a href="/messages/${message.id}/like"
                                                            style="text-decoration: none;">
                  <#if message.meLiked>
                    <i class="fas fa-heart"></i>
                  <#else>
                    <i class="far fa-heart"></i>
                  </#if>
                  ${message.likes}
            </a>
            <button class="btn btn-secondary" onclick="showHide(${message.id})" type="button"><i
                      class="fas fa-comments"></i> ${message.comments?size}</button>
            <script>
                function showHide(messNumber) {
                    var tx = "addInfo" + messNumber;
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
                <div class="row ml-4" id="messageComment">
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
                  <input class="custom-file-input" type="file" name="file" id="customFile" onchange="changeText(this)"/>
                  <label class="custom-file-label" for="customFile" id="chooseFile">Choose file</label>
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
    </div>
</@c.page>