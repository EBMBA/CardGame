package com.example.cardmicroservice.controller;


import com.example.common.model.CardDTO;
import com.example.common.model.InventoryOperationRequest;
import com.example.common.model.Enum.InventoryTransactionType;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/api/cards")
@RestController
@Slf4j
public class CardManagementRestCrt {
    @Autowired
    private CardManagementService cardService;

    @GetMapping("")
    @ResponseBody
    public List<CardDTO> getCards() {
        return cardService.getCards();
    }

    @GetMapping(value = {"/{id}"})
    @ResponseBody
    public ResponseEntity<Object> getCardById(@PathVariable String id) {
        log.info("Getting card : {}", id);
        CardDTO card = cardService.getCardByIdDTO(Integer.valueOf(id));

        if (card != null) {
            return ResponseEntity.ok(card);
        }
        log.error("Card not found for id: {}", id);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping(value = {""})
    @ResponseBody
    public ResponseEntity<Object> addCard(@RequestBody CardDTO card) {
        log.info("Adding card : {}", card);
        if (cardService.addCard(card).equals(true)) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping(value = {"/random"})
    @ResponseBody
    public ResponseEntity<Object> getRandomCard() {
        Integer id = cardService.getRandomCard();

        if (id != null) {
            return ResponseEntity.ok(id);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PutMapping(value = "/{cardId}",consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Object> updateCard(@PathVariable String cardId, @RequestBody(required = true) CardDTO card, HttpServletResponse response){

        if (cardService.updateCard(Integer.valueOf(cardId), card).equals(true)) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
    }

    @DeleteMapping(value = "/{card_id}")
    @ResponseBody
    public ResponseEntity<Object> deleteCard(@PathVariable String card_id) {
        if (cardService.deleteCard(Integer.valueOf(card_id)).equals(true)) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
    }

}
