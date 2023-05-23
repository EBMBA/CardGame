async function sendDataRegistering(){
    const name = document.getElementById("name").value ;
    const username = document.getElementById("username").value ;
    const password = document.getElementById("password").value ;
    const repassword = document.getElementById("repassword").value ;
    const status = document.getElementById("status") ;
    

    if (password != repassword) {
        status.textContent = "Passwords aren't not correctly input.";
        return
    }
    else {
        data = {
            "username": username,
            "password": password,
            "name": name
        }

        fetch("http://localhost:80/api/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (response.ok || response.status === 201) {
                    window.location.assign("/index.html");
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
