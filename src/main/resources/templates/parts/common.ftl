<#macro page>
  <!DOCTYPE html>
  <html lang="en">
  <head>
    <meta charset="UTF-8"/>
    <title>Communication</title>
    <link rel="stylesheet" href="/static/style.css"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <script src="https://kit.fontawesome.com/730674f6bd.js" crossorigin="anonymous"></script>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous"/>

  </head>
  <body>
  <#include "navbar.ftl">
  <div class="container mt-5">
  <#nested>
  </div>
  <!-- Optional JavaScript -->
  <!-- SockJS, StompJS, jQuery, then Popper.js, then Bootstrap JS -->
  <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>
  <script>
    function changeText(myFile) {
        var file = myFile.files[0];
        console.log(file.name)
        if (file.name.endsWith(".png") || file.name.endsWith(".jpg") || file.name.endsWith(".jpeg")) {
            document.getElementById("chooseFile").innerHTML = file.name;
            return;
        }
        alert("You can upload only image file");
        document.getElementById("customFile").value = "";
    }

    function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#blah').attr('src', e.target.result);
            }

            reader.readAsDataURL(input.files[0]);
        }
    }
    $("#customFile").change(function(){
        readURL(this);
    });
  </script>
  <script type="text/javascript">

      var stompClient = null;

      function setConnected(connected) {

          document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
          document.getElementById('response').innerHTML = '';
      }

      function connect() {

          var socket = new SockJS('/chat');
          stompClient = Stomp.over(socket);
          stompClient.connect({}, function (frame) {
              setConnected(true);
              console.log('Connected: ' + frame);

              let from = document.getElementById('from').value;
              let to = document.getElementById('to').value;
              let chatRoom = getChatRoom(from, to);

              stompClient.subscribe('/user/queue/messages/' + chatRoom, function (messageOutput) {
                  showMessageOutput(JSON.parse(messageOutput.body));
              });
          });
      }

      function disconnect() {

          if (stompClient != null) {
              stompClient.disconnect();
          }

          setConnected(false);
          console.log("Disconnected");
      }

      function sendMessage() {

          var from = document.getElementById('from').value;
          var text = document.getElementById('text').value;
          var to = document.getElementById('to').value;

          let chatRoom = getChatRoom(from, to);
          stompClient.send("/app/chat/" + chatRoom, {}, JSON.stringify({'from': from, 'text': text, 'to': to}));
      }

      function showMessageOutput(messageOutput) {

          var response = document.getElementById('response');
          var p = document.createElement('p');
          p.style.wordWrap = 'break-word';
          let newMessage = messageOutput.fromU + ": " + messageOutput.text + " (" + messageOutput.time + ")";
          p.setAttribute("align", "right");
          p.appendChild(document.createTextNode(newMessage));
          response.appendChild(p);
      }

  </script>
  <script>
      function getChatRoom(senderId, recipientId) {
          const url="http://localhost:8080" + "/chat/" + senderId + "/" + recipientId;
          return JSON.parse(Get(url).responseText);
      }
      function Get(requestUrl) {
          let Httpreq = new XMLHttpRequest(); // a new request
          Httpreq.open("GET", requestUrl, false);
          Httpreq.send(null);

          return Httpreq;
      }
  </script>
  </body>
  </html>
</#macro>