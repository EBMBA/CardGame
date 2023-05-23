package com.example.cardmicroservice.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.cardmicroservice.controller.CardManagementService;
import com.example.cardmicroservice.model.Card;
import com.example.cardmicroservice.model.CardDTOMapper;
import com.example.cardmicroservice.repository.CardRepository;
import com.example.common.model.CardDTO;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CardManagementService.class)
public class CardServiceTest {
    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private CardDTOMapper cardDTOMapper;

    @Autowired
    private CardManagementService cardService;

    @Captor
    private ArgumentCaptor<Card> cardCaptor;

    CardDTO newCardDTO;

    @BeforeEach 
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        newCardDTO = new CardDTO();
        newCardDTO.setCard_id("1");
        newCardDTO.setAffinity("mock");
        newCardDTO.setAttack("mock");
        newCardDTO.setDefense("mock");
        newCardDTO.setDescription("mock");
        newCardDTO.setName("mock");
        newCardDTO.setPrice(200f);
        newCardDTO.setFamily("mock");
        newCardDTO.setHp("mock");
        newCardDTO.setImgUrl("mock");
    }
    
    @Test
    public void testAddCard_NewCard_Success() {

        when(cardRepository.findByName(newCardDTO.getName()))
            .thenReturn(Optional.empty())
            .thenReturn(Optional.of(new Card()));

        boolean result = cardService.addCard(newCardDTO);

        assertTrue(result);
        verify(cardRepository).save(cardCaptor.capture());
        assertEquals(newCardDTO.getName(), cardCaptor.getValue().getName());
    }

    @Test
    public void testAddCard_ExistingCard_Failure() {
        CardDTO existingCardDTO = new CardDTO();
        existingCardDTO.setName("ExistingCard");

        when(cardRepository.findByName(existingCardDTO.getName())).thenReturn(Optional.of(new Card()));

        boolean result = cardService.addCard(existingCardDTO);

        assertFalse(result);
        verify(cardRepository, never()).save(Mockito.any(Card.class));
    }

    @Test
    public void testGetCards() {
        List<Card> cardList = new ArrayList<>();
        cardList.add(new Card());
        cardList.add(new Card());

        when(cardRepository.findAll()).thenReturn(cardList);
        when(cardDTOMapper.apply(Mockito.any(Card.class))).thenReturn(new CardDTO());

        List<CardDTO> result = cardService.getCards();

        assertNotNull(result);
        assertEquals(cardList.size(), result.size());
        verify(cardRepository).findAll();
        verify(cardDTOMapper, times(cardList.size())).apply(Mockito.any(Card.class));
    }

    @Test
    public void testDeleteCard() {
        when(cardRepository.findById(Integer.valueOf(newCardDTO.getCard_id()))).thenReturn(Optional.of( new Card(1, "mock", "mock", "mock", "mock", "mock", "mock", "mock", "mock", "mock", 200f)));

        boolean result = cardService.deleteCard(Integer.valueOf(newCardDTO.getCard_id()));

        assertTrue(result);
    }

    @Test
    public void testDeleteCard_NonExistingCard_Failure() {
        when(cardRepository.findById(Integer.valueOf(newCardDTO.getCard_id()))).thenReturn(Optional.empty());

        boolean result = cardService.deleteCard(Integer.valueOf(newCardDTO.getCard_id()));

        assertFalse(result);
    }

    @Test
    public void testUpdateCard() {
        when(cardRepository.findById(Integer.valueOf(newCardDTO.getCard_id()))).thenReturn(Optional.of( new Card(1, "mock", "mock", "mock", "mock", "mock", "mock", "mock", "mock", "mock", 200f)));

        boolean result = cardService.updateCard(1, newCardDTO);

        assertTrue(result);
    }

    @Test
    public void testUpdateCard_NonExistingCard_Failure() {
        when(cardRepository.findById(Integer.valueOf(newCardDTO.getCard_id()))).thenReturn(Optional.empty());

        boolean result = cardService.updateCard(1, newCardDTO);

        assertFalse(result);
    }
    
}
