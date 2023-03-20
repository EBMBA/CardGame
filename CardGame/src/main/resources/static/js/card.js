// Function that sends a POST request to the API to sell a card with the card ID provided as a parameter.
async function sendData(card){
    const cardId = card ;
    
    data={
        "cardId" : cardId,
    }
    fetch("http://localhost:8081/api/user/card/sell", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error("Network response was not ok");
        })
        .then(data => {
            console.log("Card id :", data);
            window.location.assign("http://localhost:8081/api/user/me");
        })
        .catch(error => {
            window.location.reload();
        });
}
