package com.example.cardmicroservice.Repository;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.cardmicroservice.model.Card;
import com.example.cardmicroservice.repository.CardRepository;

@DataJpaTest
public class CardRepositoryTest {
    @Autowired
    CardRepository cRepository;
    
    @AfterEach
    public void cleanUp() {
        cRepository.deleteAll();
    }

    @Test
    public void testFindByName() {
        Card card = new Card(1, "mock", "mock", "mock", "mock", "mock", "mock", "mock", "mock", "mock", 200f);
        cRepository.save(card);
        Optional<Card> cardE = cRepository.findByName("mock");
        assert(cardE.get().getCardId() == 1);
    } 
    
}
