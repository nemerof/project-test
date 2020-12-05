<#import "profilePicture.ftl" as p>

<#macro mess currentUserId isAdmin>
    <#list messages as message>
      <div class="container mt-3">
        <div class="card m-auto" style="width: 600px">
          <div class="row mx-4 my-3">
            <div class="col-11">
              <h5 class="card-title">
                  <@p.profilePicture message.user.profilePic 50 50/>
                <a href="/profile/${message.user.id}">${message.user.username}</a>
                ${message.postTime}
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
          </div>
        </div>
      </div>
    </#list>
</#macro>
