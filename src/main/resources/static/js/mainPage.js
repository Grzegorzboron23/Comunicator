
    let chosenUserId=null;

function sendMessage() {
    const message = document.getElementById("messageInput").value;
    const fromLogin = document.getElementById("userId").value;
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        stompClient.send("/app/chat/" + chosenUserId, {}, JSON.stringify({
            message: message,
            fromLogin: fromLogin
        }));
            showAllMessagesWithChosenUser(chosenUserId);
    });
}


 $(document).ready(function() {
      $("#searchInput").on("input", function() {
        var inputValue = $(this).val();

       $.ajax({
                  type: "POST",
                  url: "/processData",
                  data: { inputValue: inputValue },
                  success: function(response) {

                       var selectHtml = createSelect(response);
                       var selectElement = document.getElementById("userSelect");
                       selectElement.size = response.length;

                       assignUserId();

                  },
                  error: function (xhr, status, error) {
                       console.error("Error during AJAX request. Status: " + status + ", Error: " + error);
                       console.log("XHR object:", xhr);
                  },
                  complete: function () {

                  }
              });


            });
 });

 function createSelect(response) {
    var selectHtml = '<select id="userSelect" size="' + response.length + '" style="width: 200px;">';
    response.forEach(function(user) {
             selectHtml += '<option value="' + user.id + '">' + user.name + '</option>';
             });
    selectHtml += '</select>';
    $("#selectContainer").html(selectHtml);
 return selectHtml
 }

 function showInputAndButton() {
       document.getElementById("messageInput").hidden = false;
       document.getElementById("sendButton").hidden = false;
 }

 function assignUserId(){
    $("#userSelect").on("change", function() {
        var selectedUserId = $(this).val();

        if (selectedUserId !== "") {
             chosenUserId = selectedUserId;
             showInputAndButton();
             showAllMessagesWithChosenUser(selectedUserId);
             }
        });
 }
 function showAllMessagesWithChosenUser(selectedUserId){
    $.ajax({
                      type: "POST",
                      url: "/showAllMessagesWithUser",
                      data: { selectedUserId: selectedUserId },
                      success: function(response) {

                        var messagesContainer = document.getElementById("messagesContainer");
                        messagesContainer.innerHTML = "";
                        response.forEach(function(message) {
                            var messageDiv = document.createElement("div");
                            messageDiv.classList.add("message");

                            var messageText = document.createElement("p");
                            messageText.textContent = "Message: " + message.message_text;

                            var messageFrom = document.createElement("p");
                            messageFrom.textContent = "Od: " + message.message_from;

                            messageDiv.appendChild(messageText);
                            messageDiv.appendChild(messageFrom);

                            messagesContainer.appendChild(messageDiv);
                        });
                      },
                      error: function (xhr, status, error) {
                           console.error("Error during AJAX request. Status: " + status + ", Error: " + error);
                           console.log("XHR object:", xhr);
                      },
                      complete: function () {

                      }
                  });

 }



