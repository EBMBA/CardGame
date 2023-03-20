async function sendDataRegistering(){
    const name = document.getElementById("name").value ;
    const surname = document.getElementById("surname").value ;
    const password = document.getElementById("password").value ;
    const repassword = document.getElementById("repassword").value ;
    const status = document.getElementById("status") ;
    

    if (password != repassword) {
        status.textContent="Passwords aren't not correctly input.";
        return
    }
    else{
        data={
            "name" : name,
            "surname" : surname,
            "password" : password
        }

        fetch("/api/user", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (response.ok || response.status === 409) {
                    return response.json();
                }
                throw new Error("Network response was not ok");
            })
            .then(data => {
                status.textContent=data["Status"];
                if (data["Status"] == "Created"){
                    window.location.assign("/index.html");
                }
            })
            .catch(error=> {
                console.error("There was a problem with the fetch operation:", error);
            });
    }

}
