async function sendDataLogin(){
    const surname = document.getElementById("surname").value ;
    const password = document.getElementById("password").value ;
    const status = document.getElementById("status") ;


    data={
        "surname" : surname,
        "password" : password
    }

    fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok || response.status === 401) {
                return response.json();
            }
            throw new Error("Network response was not ok");
        })
        .then(data => {
            status.textContent=data["Status"];
            if (data["Status"] == "Accepted"){
                window.location.assign("/index.html");
            }
    })
        .catch(error => {
            console.error("There was a problem with the fetch operation:", error);
        });
}
