package com.example.cardmicroservice.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.cardmicroservice.controller.CardManagementRestCrt;
import com.example.cardmicroservice.controller.CardManagementService;
import com.example.common.model.CardDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CardManagementRestCrt.class)
class CardRestCRTTest {

    @MockBean
    private CardManagementService cardService;

    @Autowired
    private MockMvc mockMvc;

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
    void testGetCards() throws Exception {
        List<CardDTO> cardDTOList = new ArrayList<>();
        cardDTOList.add(newCardDTO);
        cardDTOList.add(newCardDTO);

        when(cardService.getCards()).thenReturn(cardDTOList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/cards")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void testGetCardById_ValidId_Success() throws Exception {
        when(cardService.getCardByIdDTO(1)).thenReturn(newCardDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/cards/1")
                .contentType(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void testGetCardById_InvalidId_NotFound() throws Exception {
        int id = 2;
        when(cardService.getCardByIdDTO(id)).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/cards/2")
                .contentType(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void testAddCard_ValidCard_Success() throws Exception {
        when(cardService.addCard(Mockito.any())).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newCardDTO));
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void testAddCard_InvalidCard_BadRequest() throws Exception {
        CardDTO cardDTO = new CardDTO();

        when(cardService.addCard(cardDTO)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cardDTO));
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void testGetRandomCard() throws Exception {
        int id = 1;

        when(cardService.getRandomCard()).thenReturn(id);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/cards/random")
                .contentType(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("1", result.getResponse().getContentAsString());
    }

    @Test
    void testUpdateCard_ValidCard_Success() throws Exception {
        int cardId = 1;

        when(cardService.updateCard(Mockito.anyInt(), Mockito.any())).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/cards/" + cardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newCardDTO));
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, result.getResponse().getStatus());

    }

    @Test
    void testUpdateCard_InvalidCard_NotModified() throws Exception {
        int cardId = 1;
        CardDTO cardDTO = new CardDTO();

        when(cardService.updateCard(cardId, cardDTO)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/cards/{card_id}", cardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cardDTO));
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(304, result.getResponse().getStatus());
    }

    @Test
    void testDeleteCard_ValidId_Success() throws Exception {
        int cardId = 1;

        when(cardService.deleteCard(cardId)).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/cards/{card_id}", cardId)
                .contentType(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void testDeleteCard_InvalidId_NotModified() throws Exception {
        int cardId = 1;

        when(cardService.deleteCard(cardId)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/cards/{card_id}", cardId)
                .contentType(MediaType.APPLICATION_JSON);
    
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        
        assertEquals(304, result.getResponse().getStatus());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}

