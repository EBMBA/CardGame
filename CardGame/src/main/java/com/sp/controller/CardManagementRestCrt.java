package com.sp.controller;

import com.sp.model.Card.CardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/cards")
@RestController
public class CardManagementRestCrt {
   @Autowired
   private CardManagementService cardDisplayService;
   @GetMapping("")
   @ResponseBody
   public List<CardDTO> getCards() {
       return cardDisplayService.getCards();
   }

    @GetMapping(value = {"/{id}"})
    @ResponseBody
    public ResponseEntity<Object> getCardById(@PathVariable String id) {
        CardDTO card = cardDisplayService.getCardByIdDTO(Integer.valueOf(id));
            
        if (card != null) {
            return ResponseEntity.ok(card);
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card not found");
    }

    @PostMapping(value = {""})
    @ResponseBody
    public ResponseEntity<Object> addCard(@RequestBody CardDTO card) {
        if (cardDisplayService.addCard(card).equals(true)) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    
}
