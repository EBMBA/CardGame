var cookie = document.cookie;

// console.log(cookie);

const buyButton = document.querySelector(".button-buy");
const sellButton = document.querySelector(".button-sell");

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

fetch(`/api/user/me`)
  .then((response) => response.json())
  .then((data) => {
    // Mettre à jour les champs "top-left" et "top-right" avec les informations de l'utilisateur
    // vérifie si le code retour est 400 si c'est le cas, on redirige vers la page de connexion
    if (data.status === 400) {
      window.location.href = "/login.html";
    }
    
      moneyField.textContent = `${data.money} euros`;
      usernameField.textContent = `${data.name} ${data.surname}`;
        

    

    
  })
  .catch((error) => {
    console.error(`Une erreur s'est produite : ${error}`);
  });

buyButton.addEventListener("click", () => {
    window.location.href = "/buy.html";
  });
sellButton.addEventListener("click", () => {
    window.location.href = "/sell.html";
  });


  // fonction delete cookie en utilisant la methode get /logout 
  async function deleteCookie() {
    fetch("/logout", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
        throw new Error("Network response was not ok");
      })
      .then((data) => {
        console.log(data);
        window.location.href = "/login.html";
      })
      .catch((error) => {
        console.error("There was a problem with the fetch operation:", error);
      });
  }


