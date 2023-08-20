
    let chosenUserId=null;


 function sendMessage() {
            const message = document.getElementById("messageInput").value;
            const fromLogin = document.getElementById("userId").value;

            console.log("userId = " + fromLogin);

            const socket = new SockJS('/ws');
            const stompClient = Stomp.over(socket);

            console.log("Zaraz wysle wiadomosc do usera o id= " + chosenUserId);
            stompClient.connect({}, function(frame) {
                stompClient.send("/app/chat/" + chosenUserId, {}, JSON.stringify({
                    message: message,
                    fromLogin: fromLogin
                }));
            });
        }



   $(document).ready(function() {
      $("#searchInput").on("input", function() {
        var inputValue = $(this).val();
      console.log("Wpisano literę: " + inputValue);
    console.log("Wpisano literę: " + inputValue);


       $.ajax({
                  type: "POST",
                  url: "/processData",
                  data: { inputValue: inputValue },
                  success: function(response) {
                  console.log("Jestem tutaj dziala");
                      var selectHtml = '<select id="userSelect">';

                      response.forEach(function(user) {
                          selectHtml += '<option value="' + user.id + '">' + user.name + '</option>';
                      });

                      selectHtml += '</select>';

                      $("#selectContainer").html(selectHtml);

                      $("#userSelect").on("change", function() {
                          var selectedUserId = $(this).val();

                          if (selectedUserId !== "") {
                              chosenUserId = selectedUserId;
                              showInputAndButton();
                              console.log("Wybrano użytkownika o ID: " + selectedUserId);
                          }
                      });
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
   function showInputAndButton() {
       document.getElementById("messageInput").hidden = false;
       document.getElementById("sendButton").hidden = false;
   }


