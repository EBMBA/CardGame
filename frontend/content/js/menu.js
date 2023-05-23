var cookie = document.cookie;

// console.log(cookie);

const buyButton = document.querySelector(".button-buy");
const sellButton = document.querySelector(".button-sell");
const logoutButton = document.querySelector(".logout-btn");



// // Vérifier si le cookie "userId" est défini
// if (cookie == "") {
  
//   console.log("Vous n'êtes pas connecté");
// } else {
 
//   buyButton.addEventListener("click", () => {
   
//     window.location.href = "/buy.html";
//   });
//   sellButton.addEventListener("click", () => {
    
//     window.location.href = "/sell.html";
//   });
// }


const moneyField = document.querySelector(".top-left");
const usernameField = document.querySelector(".top-right");

// fetch(`/api/user/me`)
//   .then((response) => response.json())
//   .then((data) => {
//     // Mettre à jour les champs "top-left" et "top-right" avec les informations de l'utilisateur
//     // vérifie si le code retour est 400 si c'est le cas, on redirige vers la page de connexion
//     if (data.status === 400) {
//       window.location.href = "/login.html";
//     }
//       moneyField.textContent = `${data.money} euros`;
//       usernameField.textContent = `${data.name} ${data.surname}`;
//   })
//   .catch((error) => {

    const headers = new Headers();
    headers.append("Authorization", "Bearer " + getCookieValue("jwttoken"));
    headers.append("Content-Type", "application/json");

    fetch("http://127.0.0.1.nip.io/api/users/" + getCookieValue("user_id"), {
        method: "GET",
        headers: headers
    })
    // Si le status = 200 ajouter dans le usernameField le nom (username)
    .then(data => {
      if (data.status === 200) {
        data.json().then(data => {
          usernameField.textContent = `${data.name} ${data.username}`;
        })
      }
    })
    .catch(error => {
      console.error("There was a problem with the fetch operation:", error);
    });

    // Meme chose pour le moneyField avec /api/wallets/{user_id}
    fetch("http://127.0.0.1.nip.io/api/wallets/" + getCookieValue("user_id"), {
        method: "GET",
        headers: headers
    })
    .then(data => {
      if (data.status === 200) {
        data.json().then(data => {
          moneyField.textContent = `${data.money} euros`;
        })
      }
    })
    .catch(error => {
      console.error("There was a problem with the fetch operation:", error);
    });
  

  



        

    

    
//   })
//   .catch((error) => {
//     console.error(`Une erreur s'est produite : ${error}`);
//   });

buyButton.addEventListener("click", () => {
    window.location.href = "/buy.html";
  });
sellButton.addEventListener("click", () => {
    window.location.href = "/sell.html";
  });

logoutButton.addEventListener("click", () => {
  window.location.href = "/logout.html";
});

// event listener click pour button-play
const playButton = document.querySelector(".button-play");
playButton.addEventListener("click", () => {
  window.location.href = "/game-rooms.html";
});
  // fonction delete cookie en utilisant la methode get /logout 
  // async function deleteCookie() {
  //   fetch("/logout", {
  //     method: "GET",
  //     headers: {
  //       "Content-Type": "application/json",
  //     },
  //   })
  //     .then((response) => {
  //       if (response.ok) {
  //         return response.json();
  //       }
  //       throw new Error("Network response was not ok");
  //     })
  //     .then((data) => {
  //       console.log(data);
  //       window.location.href = "/login.html";
  //     })
  //     .catch((error) => {
  //       console.error("There was a problem with the fetch operation:", error);
  //     });
  // }

  function deleteCookie() {
    document.cookie = "jwttoken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  }
  
  window.addEventListener('DOMContentLoaded', function() {
    var jwtToken = getCookieValue("jwttoken");
    console.log("Test JWT Token:", jwtToken);
    if (jwtToken !== "") {
      // Cookie is present, perform desired actions
      console.log("JWT Token:", jwtToken);
      // Additional code here...
    } else {
      // Cookie is not present, redirect to login page
      window.location.href = "/login.html";
    }
  });
  
  
  
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
  
  // Call the checkCookie function when the page loads
  window.onload = checkCookie;
  


