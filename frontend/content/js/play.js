async function sendDataRoomCreation() {
    const room_name = document.getElementById("room_name").value;
    const mise = document.getElementById("mise").value;

    data = {
        "room_name": room_name,
        "mise": mise
    }
    const headers = new Headers();
    headers.append("Authorization", "Bearer " + getCookieValue("jwttoken"));
    headers.append("Content-Type", "application/json");

    fetch("http://127.0.0.1.nip.io/api/game/room", {
        method: "POST",
        headers: headers,
        body: JSON.stringify(data)
    })

        
        .then(data => {
            // si la réponse (status) est 200, on redirige vers la page de jeu
            console.log("data=" + data);
            document.cookie = "room_name=" + room_name;
            if (data.status === 200) {
                window.location.href = "/game-wait-player.html";
            }
        })
        .catch(error => {

            console.error("There was a problem with the fetch operation:", error);
        });
}

const create = document.querySelector(".button-create");
create.addEventListener("click", () => {
    window.location.href = "/game-create-room.html";
});


// FOnction joinRoom pour rejoindre une room Get localhost:80/api/game/join/$nomdelaroom
async function joinRoom(room_name) {
    const headers = new Headers();
    headers.append("Authorization", "Bearer " + getCookieValue("jwttoken"));
    headers.append("Content-Type", "application/json");
    

    fetch("http://127.0.0.1.nip.io/api/game/join/" + room_name, {
        method: "GET",
        headers: headers
    })
        .then(data => {
            // si la réponse (status) est 200, on redirige vers la page de jeu
            console.log("data=" + data);
            
            if (data.status === 200) {
                document.cookie = "room_name=" + room_name;
                window.location.href = "/game-wait-player.html";
            }
        })
        .catch(error => {

            console.error("There was a problem with the fetch operation:", error);
        });
}

    





function getCookieValue(cookieName) {
    var name = cookieName + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var cookieArray = decodedCookie.split(';');

    for (var i = 0; i < cookieArray.length; i++) {
        var cookie = cookieArray[i];
        while (cookie.charAt(0) == ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(name) == 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }
    return "";
}

async function sendDataChooseCard(card_id) {
    data = {
        "card_id": card_id
    }
    const headers = new Headers();
    headers.append("Authorization", "Bearer " + getCookieValue("jwttoken"));
    headers.append("Content-Type", "application/json");

    fetch("http://127.0.0.1.nip.io/api/game/room/" + getCookieValue("room_name"), {
        method: "PUT",
        headers: headers,
        body: JSON.stringify(data)
    })
        .then(data => {
            // si la réponse (status) est 200, on redirige vers la page de jeu
            if (data.status === 200) {
                window.location.href = "/game-result.html";
            }
        })
        .catch(error => {

            console.error("There was a problem with the fetch operation:", error);
        });
}




