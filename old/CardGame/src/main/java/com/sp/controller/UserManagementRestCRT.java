package com.sp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sp.model.User.User;
import com.sp.model.User.UserDTO;
import com.sp.model.User.UserRegisterRequest;

@RestController
@RequestMapping("/api/users")
public class UserManagementRestCRT {
    @Autowired
    UserManagementService uAuthService;

    @PostMapping(value = {""},consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> addUser(@RequestBody UserRegisterRequest user, HttpServletResponse response) {
        Map<String, String> message = new HashMap<>();
        
        if (uAuthService.addUser(user).equals(true)) {
            message.put("Status", "Created");
            return ResponseEntity.ok(message);
        }
        message.put("Status", "Username is already taken.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    // @GetMapping(value = {"/{username}"})
    // @ResponseBody
    // public ResponseEntity<Object> getUser(@PathVariable String username,HttpServletRequest request) {
        
    //     UserDTO user = uAuthService.getUserNoPwd(username);

    //     if (user != null) {
    //         return ResponseEntity.ok(user);
    //     }
        
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
    // }

    @GetMapping(value = {""})
    @ResponseBody
    public ResponseEntity<Object> getUsers(HttpServletRequest request) {
        List<UserDTO> users= uAuthService.getAllUserNoPwd();

        if (users == null) {
            return ResponseEntity.badRequest().body("Error");
        }

        return ResponseEntity.ok().body(users);   
    }

    @PutMapping(value="/{username}")
	public ResponseEntity<Object> setUser(@PathVariable String username, @RequestBody UserRegisterRequest user) {
        
        if (! uAuthService.setUser(user,username)) {
		    return ResponseEntity.badRequest().body(null)  ;
        }
		return ResponseEntity.ok().body(null) ; 
	}
	
	@DeleteMapping(value="/{username}")
	public ResponseEntity<Object> delUser(@PathVariable String username) {
        if (!uAuthService.deleteUser(username)) {
            return ResponseEntity.ok().body("User deleted");
        }
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) ;
	}

    @GetMapping(value = {"/{user_id}"})
    @ResponseBody
    public ResponseEntity<Object> getUserID(@PathVariable String user_id,HttpServletRequest request) {
        
        UserDTO user = uAuthService.getUserNoPwdFromId(user_id);

        if (user != null) {
            return ResponseEntity.ok(user);
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
    }

    @GetMapping(value = {"/auth/{username}"})
    @ResponseBody
    public ResponseEntity<Object> getAuthUser(@PathVariable String username,HttpServletRequest request) {
        
        UserDetails user = uAuthService.loadUserByUsername(username);

        if (user != null) {

            return ResponseEntity.ok( user);
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
    }



    // @PostMapping(value = {"/login"})
    // @ResponseBody
    // public ResponseEntity<Object> loginUser(@RequestBody(required = true) Map<String, String> request , HttpServletResponse response) {
    //     String surname = request.get("surname");
    //     String password = request.get("password");
    //     Map<String, String> message = new HashMap<>();

        
    //     if (surname == null || password == null) {
    //         message.put("Status", "Surname and/or password not provided.");
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    //     }
        
    //     Integer userID =  uAuthService.getUserBySurname(surname, password);
                
    //     if (userID != null) {

    //         // create a cookie
    //         Cookie cookie = new Cookie("userId", userID.toString());

    //         // expires in 7 days
    //         // cookie.setMaxAge(7 * 24 * 60 * 60);

    //         // expires in  60 minutes
    //         cookie.setMaxAge(60 * 60);

    //         // optional properties
    //         cookie.setSecure(false);
    //         cookie.setHttpOnly(true);
    //         cookie.setPath("/");

    //         // add cookie to response
    //         response.addCookie(cookie);

    //         message.put("Status", "Accepted");

    //         return ResponseEntity.ok(message);
    //     } else {
    //         message.put("Status", "Invalid credentials");

    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    //     }

    // }

    // @GetMapping(value = {"/logout"}) // Suppresion du cookie pour l'utilisateur. 
    // @ResponseBody
    // public ResponseEntity<Object> logoutUser(HttpServletRequest request, HttpServletResponse response) {
    //     Map<String, String> message = new HashMap<>();

    //     Cookie[] cookies = request.getCookies();
    //     if (cookies != null) {
    //         for (Cookie cookie : cookies) {
    //             if (cookie.getName().equals("userId")) {
    //                 cookie.setMaxAge(0);
    //                 response.addCookie(cookie);
    //             }
    //         }
    //     }

    //     message.put("Status", "Accepted");

    //     return ResponseEntity.ok(message);
    // }

    // @PostMapping(value = {"/api/user/card/buy"})
    // @ResponseBody
    // public ResponseEntity<Object> addCard(@RequestBody(required = true)Map<String, String>request,@CookieValue("userId") Integer userIdCookie){
    //     String cardId = request.get("cardId");
    //     if(cardId == null){
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No userId or cardId");
    //     }

    //     if(userIdCookie == null ){
    //         return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized"); 
    //     }

    //     User user = uAuthService.getUser(userIdCookie);
    //     Card card = cardsService.getCardById(Integer.valueOf(cardId));

    //     if (card == null) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot buy the card, card not found");
    //     }

    //     if (this.uAuthService.addCard(user, card)){
    //         return ResponseEntity.ok("Success");
    //     }
    //     else{
    //         System.out.println(card.getCardId() + " not added to " + user.getUsername());
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot buy the card. Check if you have enough money.");
    //     }
    // }

    // @PostMapping(value = {"/api/user/card/sell"})
    // @ResponseBody
    // public ResponseEntity<Object> removeCard(@RequestBody(required = true)Map<String, String>request,@CookieValue("userId") Integer userIdCookie){
    //     String cardId = request.get("cardId");
    //     if(cardId == null){
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No cardId");
    //     }

    //     if(userIdCookie == null){
    //         return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized"); 
    //     }

    //     User user = uAuthService.getUser(userIdCookie);
    //     Card card = cardsService.getCardById(Integer.valueOf(cardId));

    //     if (card == null) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot sell the card, card not found");
    //     }

    //     if (this.uAuthService.removeCard(user, card)){
    //         return ResponseEntity.ok("Success");
    //     }
    //     else{
    //         System.out.println(card +" not deleted to "+ user);
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot sell the card.");
    //     }
    // }

    // Route vers la page menu.html présent dans le dossier static
    


   
}
