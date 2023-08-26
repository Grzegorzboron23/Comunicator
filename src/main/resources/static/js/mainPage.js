
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


                        response.forEach(function(item) {
                            createTableContents(item, table);
                        });

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
        innerContainer.style.display = "flex";
        innerContainer.style.flexDirection = "column";
        innerContainer.style.justifyContent = "center";
        innerContainer.style.alignItems = "center";


        response.forEach(function(message) {
            var messageDiv = document.createElement("div");
            messageDiv.classList.add("message");

            var messageText = document.createElement("p");
            messageText.textContent = "Message: " + message.message_text;

            var messageFrom = document.createElement("p");
            messageFrom.textContent = "Od: " + message.message_from;

            messageDiv.appendChild(messageText);
            messageDiv.appendChild(messageFrom);

            innerContainer.appendChild(messageDiv);
        });
        return innerContainer;
    }
 }

 function createTableContents(item, table){
    var row = table.insertRow();
    var cell = row.insertCell();

     if(item.senderLastMessage.id === item.loginUserId){
    cell.innerHTML = `
    <strong>${item.user.name} :</strong> <br>
    ${item.dateTime}<br>
     <strong>You :</strong> ${item.messageText}<br>
    `;
    }else{
    cell.innerHTML = `
         <strong>${item.user.name} :</strong> <br>
        ${item.dateTime}<br>
        <strong>${item.senderLastMessage.name} :</strong> ${item.messageText}<br>
        `;
    }

      row.addEventListener("click", function() {
            console.log("Kliknięto w wiersz: " + item.messageText);
            showAllMessagesWithChosenUser(item.user.id)
            showInputAndButton();
        });

}




