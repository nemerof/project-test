<#import "profilePicture.ftl" as p>

<#macro mess currentUserId isAdmin>
    <style>
        .hidden{
            display : none;
        }
    </style>
    <#list messages as message>
      <div class="container mt-3">
        <div class="card m-auto" style="width: 600px">
          <div class="row mx-4 my-3">
            <div class="col-11">
              <h5 class="card-title">
                  <@p.profilePicture message.user.profilePic 50 50/>
                <a href="/profile/${message.user.id}">${message.user.username}</a>
                  ${formatDateTime(message.postTime, 'MMM-dd-YYYY HH:mm')}
              </h5>
            </div>
            <div class="col-1" style="width:500px; float:right; text-align:left">
              <#if isAdmin || currentUserId==message.user.id>
                <a href="/delete/${message.id}" style="text-decoration: none; font-size: 30px"><i class="fas fa-trash"></i></a>
              </#if>
            </div>
          </div>
          <div class="row mr-4 ml-5 mt-1 md-4"><p class="card-text"><span>${message.text}</span></p></div>
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
            <button class="btn btn-secondary" onclick="showHide(${message.id})" type="button"><i class="fas fa-comments"></i></button>
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
                <button type="submit" class="btn btn-primary">Save message</button>
              </div>
            </form>
          </div>
          </div>
        </div>
    </#list>
</#macro>
