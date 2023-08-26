
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
      findLastConversations();

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

 function findLastConversations(){
       $.ajax({
                  type: "POST",
                  url: "/findLastConversations",
                  data: { },
                  success: function(response) {


                     var table = document.createElement("table");
                        table.classList.add("lastConversationTable");


                      for (var key in response) {
                             if (response.hasOwnProperty(key)) {
                                 createTableContents(response[key], table);
                             }
                         }

                        var container = document.getElementById("lastConversations");
                        container.innerHTML = "";
                        container.appendChild(table);
                  },
                  error: function (xhr, status, error) {
                       console.error("Error during AJAX request. Status: " + status + ", Error: " + error);
                       console.log("XHR object:", xhr);
                  },
                  complete: function () {

                  }
              });



 }

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
                        messagesContainer.style.display = "flex";
                        messagesContainer.style.flexDirection = "column";
                        messagesContainer.style.justifyContent = "flex-start";
                        messagesContainer.style.alignItems = "flex-start";
                        messagesContainer.style.overflowY = "auto";
                        messagesContainer.appendChild(createMessageContainer(messagesContainer, response));

                      },
                      error: function (xhr, status, error) {
                           console.error("Error during AJAX request. Status: " + status + ", Error: " + error);
                           console.log("XHR object:", xhr);
                      },
                      complete: function () {

                      }
                  });

    function createMessageContainer(messagesContainer, response){
        messagesContainer.innerHTML = "";



        var innerContainer = document.createElement("div");
        innerContainer.style.width = "100%";



        response.forEach(function(message) {
            var messageDiv = document.createElement("div");
            messageDiv.style.width = "100%";
            messageDiv.classList.add("message");

            var messageText = document.createElement("p");
            messageText.textContent = "Message: " + message.message;

            var messageFrom = document.createElement("p");
            messageFrom.textContent = "Od: " + message.fromLogin;

            const loggedUser = document.getElementById("loggedUser").textContent;
            console.log("test " + loggedUser + " test 2 " + message.userFrom.name);

            if (loggedUser === message.userFrom.name) {
                   messageDiv.classList.add("own-message");
                   messageDiv.appendChild(messageText);
                   messageDiv.appendChild(messageFrom);
                   }
                else {

                   messageDiv.classList.add("other-message");
                   messageDiv.appendChild(messageFrom);
                   messageDiv.appendChild(messageText);
               }

            innerContainer.appendChild(messageDiv);
        });
        return innerContainer;
    }
 }

 function createTableContents(item, table){
    var row = table.insertRow();
    var cell = row.insertCell();




    cell.innerHTML = `
         <strong>${item.userTo.name} :</strong> <br>
        ${item.dateTime}<br>
        <strong>${item.userFrom.name} :</strong> ${item.message}<br>
        `;


      row.addEventListener("click", function() {
            console.log("Kliknięto w wiersz: " + item.fromLogin);
            showAllMessagesWithChosenUser(item.toLogin)
            showInputAndButton();
        });

}




