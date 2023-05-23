package com.example.storemicroservice.Service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;

import org.apache.tomcat.jni.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.common.api.CardAPI;
import com.example.common.api.InventoryAPI;
import com.example.common.api.UserAPI;
import com.example.common.api.WalletAPI;
import com.example.common.model.CardDTO;
import com.example.common.model.InventoryOperationRequest;
import com.example.common.model.StoreOperationRequest;
import com.example.common.model.UserDTO;
import com.example.common.model.WalletTransactionRequest;
import com.example.common.model.Enum.TransactionType;
import com.example.storemicroservice.controller.StoreService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StoreService.class)
class StoreServiceTest {
    // private static final String WALLET_SERVICE_URL = "http://localhost:8080/wallet";
    // private static final String INVENTORY_SERVICE_URL = "http://localhost:8080/inventory";
    // private static final String USER_SERVICE_URL = "http://localhost:8080/user";
    // private static final String CARD_SERVICE_URL = "http://localhost:8080/card";

    private WalletAPI walletAPI = mock(WalletAPI.class, "walletAPI");
    private InventoryAPI inventoryAPI = mock(InventoryAPI.class, "inventoryAPI");
    private UserAPI userAPI = mock(UserAPI.class, "userAPI");
    private CardAPI cardAPI = mock(CardAPI.class, "cardAPI");

    @Autowired
    private StoreService storeService;

    @MockBean
    WebClient webClient;

    CardDTO expectedCardDTO;
    UserDTO expectedUserDTO;

    @BeforeEach
    public void setUp() {
        // storeService = new StoreService(WALLET_SERVICE_URL, INVENTORY_SERVICE_URL, USER_SERVICE_URL, CARD_SERVICE_URL);
        storeService.setWalletAPI(walletAPI);
        storeService.setInventoryAPI(inventoryAPI);
        storeService.setUserAPI(userAPI);
        storeService.setCardAPI(cardAPI);
        MockitoAnnotations.openMocks(walletAPI);
        MockitoAnnotations.openMocks(inventoryAPI);
        MockitoAnnotations.openMocks(userAPI);
        MockitoAnnotations.openMocks(cardAPI);
        MockitoAnnotations.openMocks(webClient);

        expectedCardDTO = new CardDTO();
        expectedCardDTO.setCard_id("1");
        expectedCardDTO.setName("mock");
        expectedCardDTO.setDescription("Mock");
        expectedCardDTO.setImgUrl("Mock");
        expectedCardDTO.setFamily("Mock");
        expectedCardDTO.setAffinity("Mock");
        expectedCardDTO.setHp("Mock");
        expectedCardDTO.setEnergy("Mock");
        expectedCardDTO.setAttack("Mock");
        expectedCardDTO.setDefense("Mock");
        expectedCardDTO.setPrice(1.0f);

        expectedUserDTO = new UserDTO();
        expectedUserDTO.setUser_id("1");
        expectedUserDTO.setName("mock");
        expectedUserDTO.setUsername("mock");
    }

    @Test
    void testDoTransactionCard_UserNotFound_ReturnsFalse() throws RestClientException, URISyntaxException {
        StoreOperationRequest operationRequest = new StoreOperationRequest("1", "1", TransactionType.BUY);


        when(userAPI.getUser(Mockito.any(Integer.class))).thenReturn(null);
        when(cardAPI.getCard(Mockito.any(Integer.class))).thenReturn(expectedCardDTO);

        Boolean result = storeService.doTransactionCard(operationRequest);

        assertFalse(result);
        verify(walletAPI, never()).updateWallet(Mockito.any(Integer.class), Mockito.any(WalletTransactionRequest.class));
        verify(inventoryAPI, never()).updateInventory(Mockito.any(Integer.class), Mockito.any(InventoryOperationRequest.class));
    }

    @Test
    void testDoTransactionCard_CardNotFound_ReturnsFalse() throws RestClientException, URISyntaxException {
        StoreOperationRequest operationRequest = new StoreOperationRequest("1", "1", TransactionType.BUY);
    
        when(cardAPI.getCard(Mockito.any(Integer.class))).thenReturn(null);
        when(userAPI.getUser(Mockito.any(Integer.class))).thenReturn(expectedUserDTO);

        Boolean result = storeService.doTransactionCard(operationRequest);

        assertFalse(result);
        verify(walletAPI, never()).updateWallet(Mockito.any(Integer.class), Mockito.any(WalletTransactionRequest.class));
        verify(inventoryAPI, never()).updateInventory(Mockito.any(Integer.class), Mockito.any(InventoryOperationRequest.class));
    }

    @Test
    void testDoTransactionCard_UserHasNotEnoughMoney_ReturnsFalse() throws RestClientException, URISyntaxException {
        StoreOperationRequest operationRequest = new StoreOperationRequest("1", "1", TransactionType.BUY);
        expectedCardDTO.setPrice(1000000.0f);
        when(cardAPI.getCard(Mockito.any(Integer.class))).thenReturn(expectedCardDTO);
        when(userAPI.getUser(Mockito.any(Integer.class))).thenReturn(expectedUserDTO);
        when(walletAPI.updateWallet(Mockito.any(Integer.class), Mockito.any(WalletTransactionRequest.class))).thenReturn(false);


        Boolean result = storeService.doTransactionCard(operationRequest);

        assertFalse(result);
        verify(inventoryAPI, never()).updateInventory(Mockito.any(Integer.class), Mockito.any(InventoryOperationRequest.class));
    }

    @Test
    void testDoTransactionCard_UserHasEnoughMoney_UserHasCard_ReturnsFalse() throws RestClientException, URISyntaxException {
        StoreOperationRequest operationRequest = new StoreOperationRequest("1", "1", TransactionType.BUY);
        when(cardAPI.getCard(Mockito.any(Integer.class))).thenReturn(expectedCardDTO);
        when(userAPI.getUser(Mockito.any(Integer.class))).thenReturn(expectedUserDTO);
        when(walletAPI.updateWallet(Mockito.any(Integer.class), Mockito.any(WalletTransactionRequest.class))).thenReturn(true);
        when(inventoryAPI.updateInventory(Mockito.any(Integer.class), Mockito.any(InventoryOperationRequest.class))).thenReturn(false);
        
        Boolean result = storeService.doTransactionCard(operationRequest);

        assertFalse(result);
    }

    @Test
    void testDoTransactionCard_UserHasEnoughMoney_UserHasNotCard_ReturnsTrue() throws RestClientException, URISyntaxException {
        StoreOperationRequest operationRequest = new StoreOperationRequest("1", "1", TransactionType.BUY);
        when(cardAPI.getCard(Mockito.any(Integer.class))).thenReturn(expectedCardDTO);
        when(userAPI.getUser(Mockito.any(Integer.class))).thenReturn(expectedUserDTO);
        when(walletAPI.updateWallet(Mockito.any(Integer.class), Mockito.any(WalletTransactionRequest.class))).thenReturn(true);
        when(inventoryAPI.updateInventory(Mockito.any(Integer.class), Mockito.any(InventoryOperationRequest.class))).thenReturn(true);
        
        Boolean result = storeService.doTransactionCard(operationRequest);

        assertTrue(result);
    }
}
