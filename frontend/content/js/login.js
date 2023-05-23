
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

async function sendDataLogin(){
    const username = document.getElementById("username").value ;
    const password = document.getElementById("password").value ;
    const status = document.getElementById("status") ;


    data={
        "username" : username,
        "password" : password
    }

    fetch("http://127.0.0.1.nip.io/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*"
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
            
            if (data != null && data != ''){
                token = data["jwttoken"]
                document.cookie = "jwttoken=" + token;

                // Get user id by POST request on /api/auth/user with the data {"token": token}
                console.log("getting user id", token)
                getUserId(token);
                   
            }
    })
        .catch(error => {
            console.error("There was a problem with the fetch operation:", error);
        });
}

// Get user id by POST request on /api/auth/user with the data {"token": token}
async function getUserId(token){
    console.log("getting user id 2", token)

    // headers = {
    //     "Authorization": "Bearer " + btoa(token),
    //     "Content-Type": "application/json",
    //     "Access-Control-Allow-Origin": "*"
    // }

    // console.log(headers)

    const headers = new Headers();
    headers.append("Authorization", "Bearer " + getCookieValue("jwttoken"));
    headers.append("Content-Type", "application/json");

    fetch("http://127.0.0.1.nip.io/api/auth/user", {
        method: "POST",
        headers: headers,
        // mode: "no-cors",
        body: JSON.stringify({"token": token})
    })
        .then(response => {
            if (response.ok) {
                // console.log(response.json())
                return response.json();
            }
            throw new Error("Network response was not ok");
        })
        .then(data => {
            if (data != null && data != ''){
                user_id = data["userId"]
                document.cookie = "user_id=" + user_id;
                // Redirect to index.html
                window.location.assign("/index.html");
            }
        })
        .catch(error => {
            console.error("There was a problem with the fetch operation:", error);
        });
}

