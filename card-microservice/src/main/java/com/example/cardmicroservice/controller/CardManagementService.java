package com.example.cardmicroservice.controller;


import com.example.cardmicroservice.model.Card;
import com.example.cardmicroservice.model.CardDTOMapper;
import com.example.cardmicroservice.repository.CardRepository;
import com.example.common.model.CardDTO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CardManagementService {

    @Autowired
    CardRepository myCardRepository;

    @Autowired
    CardDTOMapper cardDTOMapper;

    public Boolean addCard(CardDTO newCard) {

        Optional<Card> cardOptional = myCardRepository.findByName(newCard.getName());

        if (cardOptional.isPresent()) {
            log.error("Card already exists {} ", newCard);
            return false;
        }

        Card card = new Card();

        card.setName(newCard.getName());
        card.setDescription(newCard.getDescription());
        card.setImgUrl(newCard.getImgUrl());
        card.setFamily(newCard.getFamily());
        card.setAffinity(newCard.getAffinity());
        card.setHp(newCard.getHp());
        card.setEnergy(newCard.getEnergy());
        card.setAttack(newCard.getAttack());
        card.setDefense(newCard.getDefense());
        card.setPrice(newCard.getPrice());

        myCardRepository.save(card);

        cardOptional = myCardRepository.findByName(newCard.getName());
        
        if(Boolean.FALSE.equals(cardOptional.isPresent())) { 
            log.error("Error saving card {} ", newCard);
            return false; 
        }

        log.info("Card saved {} ", newCard);
        return true;
    }

    public List<CardDTO> getCards() {
        List<Card> Card = new ArrayList<>();
        myCardRepository.findAll().forEach(Card::add);
        return Card.stream().map(cardDTOMapper).collect(Collectors.toList());
    }

    public CardDTO getCardByIdDTO(Integer id) {
        Optional<Card> cOptional = myCardRepository.findById(id);
        if (cOptional.isPresent()) {
            return cardDTOMapper.apply(cOptional.get());
        }else {
            log.error("Card not found for id: {}", id);
            return null;
        }

    }

    public Card getCardById(Integer id) {
        Optional<Card> cOptional = myCardRepository.findById(id);
        if (cOptional.isPresent()) {
            return cOptional.get();
        }else {
            log.error("Card not found for id: {}", id);
            return null;
        }

    }

    public Set<Card> getRandomCards(Integer number){
        int numberCards = (int) myCardRepository.count();
        Set<Card> cards = new HashSet<>();

        for (int i = 0; i < number; i++) {
            Random rand = new Random();

            // Obtain a number between [0 - 49].
            int id = rand.nextInt(numberCards);

            // Add 1 to the result to get a number from the required range
            // (i.e., [1 - 50]).
            id += 1;

            cards.add(myCardRepository.findById(id).get());
        }

        return cards;
    }

    public Integer getRandomCard(){
        int numberCards = (int) myCardRepository.count();

        Random rand = new Random();

        // Obtain a number between [0 - card's number-1].
        int id = rand.nextInt(numberCards);

        // Add 1 to the result to get a number from the required range
        // (i.e., [1 - card's number]).
        id += 1;
        return id;
    }

    public Boolean updateCard(Integer cardId,  CardDTO card){

        Optional<Card> cardOptional = myCardRepository.findById(cardId);
        if (! cardOptional.isPresent()){
            log.error("Card not found for id: {}", cardId);
            return addCard(card);
        }

        Card cardToUpdate = cardOptional.get();
        cardToUpdate.setName(card.getName());
        cardToUpdate.setDescription(card.getDescription());
        cardToUpdate.setImgUrl(card.getImgUrl());
        cardToUpdate.setFamily(card.getFamily());
        cardToUpdate.setAffinity(card.getAffinity());
        cardToUpdate.setHp(card.getHp());
        cardToUpdate.setEnergy(card.getEnergy());
        cardToUpdate.setAttack(card.getAttack());
        cardToUpdate.setDefense(card.getDefense());
        cardToUpdate.setPrice(card.getPrice());

        myCardRepository.save(cardToUpdate);

        cardOptional = myCardRepository.findById(cardId);

        if (Boolean.TRUE.equals(cardOptional.isPresent())) {
            log.info("Card updated {} ", card);
            return true;
        }
        log.error("Error updating card {} ", card);
        return false;
    }

    public Boolean deleteCard(Integer cardId){
        Optional<Card> cardOptional = myCardRepository.findById(cardId);
        if (Boolean.FALSE.equals(cardOptional.isPresent())){
            log.error("Card not found for id: {}", cardId);
            return false;
        }
        myCardRepository.deleteById(cardId);
        log.info("Card deleted {} ", cardId);
        return true;
    }



}
