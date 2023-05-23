// Function that sends a POST request to the API to purchase a card with the card ID provided as a parameter.
async function sendData(card) {
    const cardId =  ""+card;

    console.log("card id", cardId)

    data = {
        "user_id": getCookieValue("user_id"),
        "card_id": cardId,
        "transaction": "BUY"
    }

    const headers = new Headers();
    headers.append("Authorization", "Bearer " + getCookieValue("jwttoken"));
    headers.append("Content-Type", "application/json");

    fetch("http://127.0.0.1.nip.io/api/store", {
        method: "POST",
        headers: headers,
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error("Network response was not ok");
        })
        .catch(error => { console.log(error) })
        // .catch(error => {
        //     window.location.reload();
        // });
}



function getCookieValue(cookieName) {
    var name = cookieName + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var cookieArray = decodedCookie.split(';');
    
    for(var i = 0; i < cookieArray.length; i++) {
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
  