package com.example.usermicroservice.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.common.api.InventoryAPI;
import com.example.common.api.WalletAPI;
import com.example.common.model.InventoryCreationRequest;
import com.example.common.model.UserDTO;
import com.example.common.model.UserRegisterRequest;
import com.example.common.model.UserRegisterResponse;
import com.example.common.model.WalletOperationRequest;
import com.example.usermicroservice.controller.UserManagementService;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.model.UserDTOMapper;
import com.example.usermicroservice.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
// @SpringBootTest
@WebMvcTest(UserManagementService.class)
class UserManagementServiceTest {

    @MockBean
    private UserRepository uRepository;

    private WalletAPI walletAPI = mock(WalletAPI.class, "walletAPI");

    private InventoryAPI inventoryAPI = mock(InventoryAPI.class, "inventoryAPI");

    @MockBean
    private UserDTOMapper userDTOMapper;

    @Autowired
    private UserManagementService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService.setWalletAPI(walletAPI);
        userService.setInventoryAPI(inventoryAPI);
    }

    @Test
    void testRegister_NewUser_Success() {
        UserRegisterRequest userRequest = new UserRegisterRequest("mock", "mock", "testPassword");
        User newUser = new User();
        newUser.setUsername("mock");
        newUser.setName("mock");
        newUser.setUserId(1);

        when(uRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty()).thenReturn(Optional.of(newUser));
        when(uRepository.save(any(User.class))).thenReturn(newUser);
        when(walletAPI.createWallet(any(WalletOperationRequest.class))).thenReturn(true);
        when(inventoryAPI.createInventory(any(InventoryCreationRequest.class))).thenReturn(true);

        UserRegisterResponse response = userService.register(userRequest);

        assertNotNull(response);
        assertEquals(newUser.getUserId(), response.getUserId());
        verify(uRepository, times(2)).findByUsername("mock");
        verify(uRepository, times(1)).save(any(User.class));
        verify(walletAPI, times(1)).createWallet(any(WalletOperationRequest.class));
        verify(inventoryAPI, times(1)).createInventory(any(InventoryCreationRequest.class));
    }

    @Test
    void testRegister_ExistingUser_Failure() {
        UserRegisterRequest userRequest = new UserRegisterRequest("mock", "mock", "testPassword");
        User existingUser = new User();
        existingUser.setUsername("mock");
        existingUser.setName("mock");

        when(uRepository.findByUsername("mock")).thenReturn(Optional.of(existingUser));

        UserRegisterResponse response = userService.register(userRequest);

        assertNull(response);
        verify(uRepository, times(1)).findByUsername("mock");
        verify(uRepository, never()).save(any(User.class));
        verify(walletAPI, never()).createWallet(any(WalletOperationRequest.class));
        verify(inventoryAPI, never()).createInventory(any(InventoryCreationRequest.class));
    }

    @Test
    void testDeleteUser_UserExists_Success() {
        Integer userId = 1;
        User existingUser = new User();
        existingUser.setUserId(userId);

        when(uRepository.findByUserId(userId)).thenReturn(Optional.of(existingUser));
        when(walletAPI.deleteWallet(userId)).thenReturn(true);
        when(inventoryAPI.deleteInventory(userId)).thenReturn(true);
        when(uRepository.save(existingUser)).thenReturn(existingUser);

        boolean result = userService.deleteUser(userId);

        assertTrue(result);
        verify(uRepository, times(3)).findByUserId(userId);
        verify(uRepository, times(1)).delete(existingUser);
        verify(walletAPI, times(1)).deleteWallet(userId);
        verify(inventoryAPI, times(1)).deleteInventory(userId);
    }

    @Test
    void testDeleteUser_UserNotExists_Failure() {
        Integer userId = 1;

        when(uRepository.findByUserId(userId)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(userId);

        assertFalse(result);
        verify(uRepository, times(1)).findByUserId(userId);
        verify(uRepository, never()).delete(any(User.class));
        verify(walletAPI, never()).deleteWallet(userId);
        verify(inventoryAPI, never()).deleteInventory(userId);
    }

    @Test
    void testGetUser_UserExists_Success() {
        Integer userId = 1;
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setUsername("mock");
        existingUser.setName("mock");

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setUser_id(userId.toString());
        expectedUserDTO.setUsername("mock");
        expectedUserDTO.setName("mock");

        when(uRepository.findByUserId(userId)).thenReturn(Optional.of(existingUser));
        when(userDTOMapper.apply(existingUser)).thenReturn(expectedUserDTO);

        UserDTO userDTOResult = userService.getUser(userId);

        assertNotNull(userDTOResult);
        assertEquals(userId, Integer.valueOf(userDTOResult.getUser_id()));
        verify(uRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetUser_UserNotExists_Failure() {
        Integer userId = 1;

        when(uRepository.findByUserId(userId)).thenReturn(Optional.empty());

        UserDTO userDTOResult = userService.getUser(userId);

        assertNull(userDTOResult);
        verify(uRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testUpdateUser_UserExists_Success() {
        Integer userId = 1;
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setUsername("mock");
        existingUser.setName("mock");

        UserRegisterRequest userRequest = new UserRegisterRequest("mock", "mock", "testPassword");

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setUser_id(userId.toString());
        expectedUserDTO.setUsername("mock");
        expectedUserDTO.setName("mock");

        when(uRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userDTOMapper.apply(existingUser)).thenReturn(expectedUserDTO);
        when(uRepository.save(existingUser)).thenReturn(existingUser);

        Boolean userUpdateResult = userService.updateUser(userRequest ,userId);

        assertTrue(userUpdateResult);
        verify(uRepository, times(2)).findById(userId);
        verify(uRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotExists_Failure() {
        Integer userId = 1;
        UserRegisterRequest userRequest = new UserRegisterRequest("mock", "mock", "testPassword");

        when(uRepository.findById(userId)).thenReturn(Optional.empty());

        Boolean userUpdateResult = userService.updateUser(userRequest ,userId);

        assertFalse(userUpdateResult);
        verify(uRepository, times(1)).findById(userId);
        verify(uRepository, never()).save(any(User.class));
    }

    @Test
    void testGetAllUsers_Success() {
        Integer userId = 1;
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setUsername("mock");
        existingUser.setName("mock");

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setUser_id(userId.toString());
        expectedUserDTO.setUsername("mock");
        expectedUserDTO.setName("mock");

        List<User> userList = new ArrayList<>();
        userList.add(existingUser);

        when(uRepository.findAll()).thenReturn(userList);
        when(userDTOMapper.apply(existingUser)).thenReturn(expectedUserDTO);

        List<UserDTO> userDTOResult = userService.getUsers();

        assertNotNull(userDTOResult);
        assertEquals(1, userDTOResult.size());
        assertEquals(userId, Integer.valueOf(userDTOResult.get(0).getUser_id()));
        verify(uRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsers_NoUsers_Success() {
        List<User> userList = new ArrayList<>();

        when(uRepository.findAll()).thenReturn(userList);

        List<UserDTO> userDTOResult = userService.getUsers();

        assertNotNull(userDTOResult);
        assertEquals(0, userDTOResult.size());
        verify(uRepository, times(1)).findAll();
    }
}
