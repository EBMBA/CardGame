package com.sp.controller;
import com.sp.model.Card.Card;
import com.sp.model.Card.CardDTO;
import com.sp.model.Card.CardDTOMapper;
import com.sp.repository.CardRepository;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CardManagementService {

   @Autowired
   CardRepository myCardRepository;

   @Autowired
   CardDTOMapper cardDTOMapper;

   public Boolean addCard(CardDTO newCard) {

      Optional<Card> cardOptional = myCardRepository.findByName(newCard.getName());

      if ( cardOptional.isPresent()) {
         System.out.println("Error card already exists: " + newCard);
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



      try {
         myCardRepository.save(card);
         System.out.println(newCard);
         return true;
      } catch (Exception e) {
         System.out.println(e.getMessage()); 
         return false;
      }
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
            System.out.println("card " + id + " not found ");
			   return null;
		   }

   }

   public Card getCardById(Integer id) {
      Optional<Card> cOptional = myCardRepository.findById(id);
      if (cOptional.isPresent()) {
         return cOptional.get();
      }else {
         System.out.println("card " + id + " not found ");
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

   public Set<Integer> getRandomCardID(Integer number){
      int numberCards = (int) myCardRepository.count();
      Set<Integer> cardsId = new HashSet<>();

      for (int i = 0; i < number; i++) {
         Random rand = new Random();

         // Obtain a number between [0 - card's number-1].
         int id = rand.nextInt(numberCards);
   
         // Add 1 to the result to get a number from the required range
         // (i.e., [1 - card's number]).
         id += 1;

         cardsId.add(id);
      }

      return cardsId;
   }

}
