<#import "parts/common.ftl" as c>
<#import "parts/profilePicture.ftl" as p>

<@c.page>
  <div class="row border-0">
    <div class="col">
      <h5 class="mt-2 ml-4">Select user to chat with:</h5>
      <form method="get" action="/chat" class="form-inline">
        <label>
          <input type="text" name="userFilter" class="form-control ml-4" placeholder="Search for user" style="width: 150px;"/>
        </label>
        <button type="submit" class="btn btn-primary ml-2">Search</button>
      </form>
        <#list users.content as user>
            <div class="card my-1" style="height: 70px; width: 200px; margin-left: 21px;">
              <a class="card-block stretched-link text-decoration-none" href="/chat?username=${user.username}">
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
      <h5>Chat with: ${chatWuser}</h5>

        <div class="chat-header clearfix">
          <div class="chat-about">
            <div class="chat-with" id="selectedUserId"></div>
            <div class="chat-num-messages"></div>
          </div>
        </div> <!-- end chat-header -->

        <div class="chat-history">
          <ul>

          </ul>
        </div> <!-- end chat-history -->

        <div class="chat-message clearfix">
          <textarea id="message-to-send" name="message-to-send" placeholder="Type your message"></textarea>
          <button id="sendBtn">Send</button>
        </div> <!-- end chat-message -->

      </div> <!-- end chat -->
  </div>
</@c.page>