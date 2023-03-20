package com.sp.controller;
import com.sp.model.Card;
import com.sp.repository.CardRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CardManagementService {

   @Autowired
   CardRepository myCardRepository;

   public Boolean addCard(Card newCard) {
      try {
         Card createdCard = myCardRepository.save(newCard);
         System.out.println(createdCard);
         return true;
      } catch (Exception e) {
         System.out.println(e.getMessage()); 
         return false;
      }
  }  

   public List<Card> getCards() {
       List<Card> Card = new ArrayList<>();
       myCardRepository.findAll().forEach(Card::add);
       return Card;
   }

   public Card getCardById(Integer id) {
         Optional<Card> cOptional = myCardRepository.findById(id);
		   if (cOptional.isPresent()) {
            System.out.println(cOptional.get() + " found ");
			   return cOptional.get();
		   }else {
            System.out.println("card " + id + " not found ");
			   return null;
		   }

   }
}
