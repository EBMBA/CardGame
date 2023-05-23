package com.wallet.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder.In;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.common.api.UserAPI;
import com.example.common.model.UserDTO;
import com.example.common.model.WalletDTO;
import com.example.common.model.WalletTransactionRequest;
import com.wallet.controller.WalletManagementService;
import com.wallet.model.Wallet.Wallet;
import com.wallet.model.Wallet.WalletMapper;
import com.wallet.repository.WalletRepository;

import java.net.URI;
import java.util.ArrayList;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WalletManagementService.class)
public class WalletManagementServiceTest {
    
    @MockBean
    private WalletRepository wRepository;


    UserAPI userAPI = mock(UserAPI.class, "userAPI");

    @MockBean
    private WebClient webClient;

    @Autowired
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
        Mockito.when(userAPI.getUser(Mockito.any(Integer.class))).thenReturn(userDTO);
        wService.setUserAPI(userAPI);
        MockitoAnnotations.openMocks(userAPI);
        MockitoAnnotations.openMocks(wRepository);
        MockitoAnnotations.openMocks(webClient);
    }

    @Test
    public void getWallet() {
        Mockito.when(
            wRepository.findByUserId(Mockito.any(Integer.class))
            ).thenReturn(wallet);
        userAPI.setUSER_SERVICE_URL("s");

        WalletDTO walletDTO = wService.getWallet(1);
        assert(walletDTO.getClass() == WalletDTO.class);
        assert(walletDTO.getMoney() == wallet.getMoney());
    }

    @Test
    public void testDoTransactionRequest_WithEnoughMoney() {
        Float transactionAmount = 100f;
        Float initialMoney = wallet.getMoney();
        when(wRepository.findByUserId(wallet.getUserId())).thenReturn(wallet);
        when(wRepository.save(wallet)).thenReturn(wallet);

        WalletTransactionRequest transactionRequest = new WalletTransactionRequest(transactionAmount);

        boolean result = wService.doTransactionRequest(wallet.getUserId(), transactionRequest);

        assertTrue(result);
        assertEquals(initialMoney + transactionAmount, wallet.getMoney());
        verify(wRepository, times(1)).save(wallet);
    }

    @Test
    public void testDoTransactionRequest_NotEnoughMoney() {
        Float initialMoney = wallet.getMoney();
        Float transactionAmount = -2000f;

        when(wRepository.findByUserId(wallet.getUserId())).thenReturn(wallet);
        when(wRepository.save(wallet)).thenReturn(wallet);

        WalletTransactionRequest transactionRequest = new WalletTransactionRequest(transactionAmount);

        boolean result = wService.doTransactionRequest(wallet.getUserId(), transactionRequest);

        assertFalse(result);
        assertEquals(initialMoney, wallet.getMoney());
        verify(wRepository, never()).save(wallet);
    }

    @Test
    public void testDoTransactionRequest_NoWalletFound() {
        when(wRepository.findByUserId(wallet.getUserId())).thenReturn(null);

        WalletTransactionRequest transactionRequest = new WalletTransactionRequest(200f);

        boolean result = wService.doTransactionRequest(wallet.getUserId(), transactionRequest);

        assertFalse(result);
        verify(wRepository, never()).save(Mockito.any(Wallet.class));
    }

    @Test
    public void testDeleteWallet_WalletFound() {
        MockitoAnnotations.openMocks(this);
        when(wRepository.findByUserId(wallet.getUserId())).thenReturn(wallet).thenReturn(null);

        boolean result = wService.deleteWallet(wallet.getUserId());

        assertTrue(result);
        verify(wRepository, times(1)).delete(wallet);
    }

    @Test
    public void testDeleteWallet_WalletNotFound() {
        when(wRepository.findByUserId(wallet.getUserId())).thenReturn(null);


        boolean result = wService.deleteWallet(wallet.getUserId());

        assertFalse(result);
        verify(wRepository, never()).delete(Mockito.any(Wallet.class));
    }
}
