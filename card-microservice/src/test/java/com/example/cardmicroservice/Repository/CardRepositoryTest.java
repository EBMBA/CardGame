package com.example.cardmicroservice.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.cardmicroservice.model.Card;
import com.example.cardmicroservice.repository.CardRepository;

@DataJpaTest
class CardRepositoryTest {
    @Autowired
    CardRepository cRepository;
    
    @AfterEach
    public void cleanUp() {
        cRepository.deleteAll();
    }

    @Test
    void testFindByName() {
        Card card = new Card(0, "mock", "mock", "mock", "mock", "mock", "mock", "mock", "mock", "mock", 200f);
        cRepository.save(card);
        Optional<Card> cardE = cRepository.findByName("mock");
        System.out.println(cardE.get().toString());
        assertEquals(151,cardE.get().getCardId());
    } 
    
}
