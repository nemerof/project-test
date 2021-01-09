<#import "parts/common.ftl" as c>
<#import "parts/profilePicture.ftl" as p>

<@c.page>
  <body onload="connect();" onclose="disconnect();">
  <div class="row border-0">
    <div class="col">
      <h5 class="mt-2 ml-4">Select user to chat with:</h5>
      <form method="get" action="/chat" class="form-inline">
        <label>
          <input type="text" name="userFilter" class="form-control ml-4" placeholder="Search for user"
                 style="width: 150px;"/>
        </label>
        <button type="submit" class="btn btn-primary ml-2">Search</button>
      </form>
        <#list users.content as user>
          <div class="card my-1" style="height: 70px; width: 200px; margin-left: 21px;">
            <a class="card-block stretched-link text-decoration-none" href="/chat?username=${user.username}"
               onclick="connect();">
              <div class="row my-2">
                <div class="card ml-4" style="width: 52px;">
                    <@p.profilePicture user.profilePic 50 50/>
                </div>
                <h5 class="card-title mx-3">
                    ${user.username}
                </h5>
              </div>
            </a>
          </div>
        </#list>
    </div>

    <div class="col">
      <h5>Chat with: ${chatWithUser}</h5>

      <!-- Hidden. Needed only for proper chat work -->

        <#if chatWithUser != "none">
          <div id="conversationDiv">
              <#list chatMessages as msg>
                  <#if msg.fromU = currentUser.username>
                    <div class="card" align="right">
                        ${msg.fromU}: ${msg.text} (${msg.time})
                    </div>
                  <#else>
                    <div class="card" align="left">
                        ${msg.fromU}: ${msg.text} (${msg.time})
                    </div>
                  </#if>
              </#list>
            <p id="response">
            </p>
            <label style="width: 75%">
              <input class="form-control" type="text" id="text" placeholder="Write a message..."
                     style=""/>
            </label>
            <button class="btn btn-primary" id="sendMessage" onclick="sendMessage();" style="vertical-align: 0px;">
              Send
            </button>
          </div>
          <input type="hidden" id="from" value="${currentUser.username}"/>
          <input type="hidden" id="to" value="${chatWithUser}"/>
        </#if>
    </div>
  </div>
  </body>
</@c.page>