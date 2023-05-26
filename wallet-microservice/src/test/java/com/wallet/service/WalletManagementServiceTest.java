package com.wallet.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.example.common.Exception.UserNotFoundException;
import com.example.common.Exception.WalletNotFoundException;
import com.example.common.api.UserAPI;
import com.example.common.model.UserDTO;
import com.example.common.model.WalletDTO;
import com.example.common.model.WalletTransactionRequest;
import com.wallet.controller.WalletManagementService;
import com.wallet.model.Wallet;
import com.wallet.model.WalletMapper;
import com.wallet.repository.WalletRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WalletManagementService.class)
class WalletManagementServiceTest {
    
    @MockBean
    private WalletRepository wRepository;


    UserAPI userAPI = mock(UserAPI.class, "userAPI");

    @MockBean
    private WebClient webClient;

    @MockBean
    private WalletMapper wMapper;

    @Autowired
    private WalletManagementService wService;


    WalletDTO walletDTO = new WalletDTO();
    Wallet wallet = new Wallet(1, 1000f, 1);
    UserDTO userDTO = new UserDTO();

    @BeforeEach
    public void setUp() throws UserNotFoundException {
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
        MockitoAnnotations.openMocks(wMapper);
    }

    @Test
    void getWallet() {
        Mockito.when(
            wRepository.findByUserId(Mockito.any(Integer.class))
            ).thenReturn(wallet);
        Mockito.when(
            wMapper.toDTO(Mockito.any(Wallet.class))
            ).thenReturn(walletDTO);
        userAPI.setuserServiceUrl("s");

        try{
            WalletDTO walletDTOResult = wService.getWallet(1);
            assert(walletDTOResult.getClass() == WalletDTO.class);
            assertEquals(wallet.getMoney(), walletDTOResult.getMoney());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    void testDoTransactionRequest_WithEnoughMoney() {
        Float transactionAmount = 100f;
        Float initialMoney = wallet.getMoney();
        when(wRepository.findByUserId(wallet.getUserId())).thenReturn(wallet);
        when(wRepository.save(wallet)).thenReturn(wallet);

        WalletTransactionRequest transactionRequest = new WalletTransactionRequest(transactionAmount);

        boolean result;
        try {
            result = wService.doTransactionRequest(wallet.getUserId(), transactionRequest);
            assertTrue(result);
            assertEquals(initialMoney + transactionAmount, wallet.getMoney());
        } catch (WalletNotFoundException e) {
            fail("Exception thrown");
        }

        verify(wRepository, times(1)).save(wallet);
    }

    @Test
    void testDoTransactionRequest_NotEnoughMoney() {
        Float initialMoney = wallet.getMoney();
        Float transactionAmount = -2000f;

        when(wRepository.findByUserId(wallet.getUserId())).thenReturn(wallet);
        when(wRepository.save(wallet)).thenReturn(wallet);

        WalletTransactionRequest transactionRequest = new WalletTransactionRequest(transactionAmount);

        boolean result;
        try {
            result = wService.doTransactionRequest(wallet.getUserId(), transactionRequest);
            assertFalse(result);
        } catch (WalletNotFoundException e) {
            fail("Exception thrown");
        }
        
        assertEquals(initialMoney, wallet.getMoney());
        verify(wRepository, never()).save(wallet);
    }

    @Test
    void testDoTransactionRequest_NoWalletFound() {
        when(wRepository.findByUserId(wallet.getUserId())).thenReturn(null);

        WalletTransactionRequest transactionRequest = new WalletTransactionRequest(200f);
        boolean result;
        try {
            result = wService.doTransactionRequest(wallet.getUserId(), transactionRequest);
            fail("Exception not thrown");
        } catch (WalletNotFoundException e) {
            assertEquals("Wallet not found for user id: " + wallet.getUserId(), e.getMessage());
        }

        verify(wRepository, never()).save(Mockito.any(Wallet.class));
    }

    @Test
    void testDeleteWallet_WalletFound() {
        MockitoAnnotations.openMocks(this);
        when(wRepository.findByUserId(wallet.getUserId())).thenReturn(wallet).thenReturn(null);

        boolean result;
        try {
            result = wService.deleteWallet(wallet.getUserId());
            assertTrue(result);
        } catch (WalletNotFoundException e) {
            fail("Exception thrown");
        }

        verify(wRepository, times(1)).delete(wallet);
    }

    @Test
    void testDeleteWallet_WalletNotFound() {
        when(wRepository.findByUserId(wallet.getUserId())).thenReturn(null);


        boolean result;
        try {
            result = wService.deleteWallet(wallet.getUserId());
            fail("Exception not thrown");
        } catch (WalletNotFoundException e) {
            assertEquals("Wallet not found for user id: " + wallet.getUserId(), e.getMessage());
        }

        verify(wRepository, never()).delete(Mockito.any(Wallet.class));
    }
}
