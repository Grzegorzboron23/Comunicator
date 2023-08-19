 function sendMessage() {
            const message = document.getElementById("messageInput").value;
            const fromLogin = document.getElementById("userId").value;

            console.log("userId = " + fromLogin);

            const socket = new SockJS('/ws');
            const stompClient = Stomp.over(socket);

            stompClient.connect({}, function(frame) {
                stompClient.send("/app/chat/6", {}, JSON.stringify({
                    message: message,
                    fromLogin: fromLogin
                }));
            });
        }
