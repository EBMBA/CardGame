package com.sp.controller;

import com.sp.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardManagementRestCrt {
   @Autowired
   private CardManagementService cardDisplayService;
   @GetMapping("/api/cards")
   @ResponseBody
   public List<Card> getCardsForSale() {
       return cardDisplayService.getCards();
   }

    @GetMapping(value = {"/api/card/{id}"})
    @ResponseBody
    public ResponseEntity<Object> getCardById(@PathVariable String id) {
        Card card = cardDisplayService.getCardById(Integer.valueOf(id));
            
        if (card != null) {
            return ResponseEntity.ok(card);
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card not found");
    }

    @PostMapping(value = {"/api/card"})
    @ResponseBody
    public ResponseEntity<Object> addCard(@RequestBody Card card) {
        if (cardDisplayService.addCard(card).equals(true)) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    
}
