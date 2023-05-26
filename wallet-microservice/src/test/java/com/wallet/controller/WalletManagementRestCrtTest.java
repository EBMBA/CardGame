package com.wallet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.swing.Spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.SerializationUtils;

import com.example.common.model.UserDTO;
import com.example.common.model.WalletDTO;
import com.example.common.model.WalletTransactionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.controller.WalletManagementRestCrt;
import com.wallet.controller.WalletManagementService;
import com.wallet.model.Wallet;
import com.wallet.repository.WalletRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WalletManagementRestCrt.class)
class WalletManagementRestCrtTest {
    @Autowired 
    private MockMvc mvc;

    @MockBean
    private WalletManagementService wService;

    WalletDTO walletDTO = new WalletDTO();
    Wallet wallet = new Wallet(1, 1000f, 1);
    UserDTO userDTO = new UserDTO();

    @BeforeEach
    public void setUp() {
        System.out.println("[BEFORE TEST] -- Add wallet to test");
        userDTO.setUser_id("1");
        userDTO.setName("test");
        userDTO.setUsername("test");
        walletDTO.setUser(userDTO);
        walletDTO.setMoney(1000f);
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetWallet() throws Exception {

        when(wService.getWallet(1)).thenReturn(walletDTO);
        RequestBuilder request = MockMvcRequestBuilders.get("/api/wallets/1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andReturn();
        System.out.println(result.getResponse().getContentAsString());
        // expected: { "user": { "user_id": "1", "username": "test", "name": "test" },"money": 1000.0}
        String expected = "{\"user\":{\"user_id\":\"1\",\"username\":\"test\",\"name\":\"test\"},\"money\":1000.0}";
        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    void testDoTransaction_RequestAccepted() throws Exception {
        WalletTransactionRequest transactionRequest = new WalletTransactionRequest(100.0f);
        String userId = "1";

        when(wService.doTransactionRequest(Mockito.any(Integer.class), Mockito.any()))
            .thenReturn(true);

        RequestBuilder request = MockMvcRequestBuilders.put("/api/wallets/{user_id}", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(transactionRequest.toString())
            .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mvc.perform(request)
            .andReturn();

        assertEquals(202, result.getResponse().getStatus());
        verify(wService, times(1)).doTransactionRequest(Mockito.any(), Mockito.any());
    }

    @Test
    void testDoTransaction_RequestNotModified() throws Exception {
        String userId = "1";
        WalletTransactionRequest transactionRequest = new WalletTransactionRequest(100.0f);

        when(wService.doTransactionRequest(1, transactionRequest))
            .thenReturn(false);

        RequestBuilder request = MockMvcRequestBuilders.put("/api/wallets/{user_id}", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(transactionRequest))
        .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mvc.perform(request)
            .andReturn();

        assertEquals(304, result.getResponse().getStatus());
        verify(wService, times(1)).doTransactionRequest(Mockito.any(), Mockito.any());
    }

    private String asJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
