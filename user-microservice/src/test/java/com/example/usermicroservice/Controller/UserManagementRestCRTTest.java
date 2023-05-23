package com.example.usermicroservice.Controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.common.model.UserDTO;
import com.example.common.model.UserRegisterRequest;
import com.example.common.model.UserRegisterResponse;
import com.example.usermicroservice.controller.UserManagementRestCRT;
import com.example.usermicroservice.controller.UserManagementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserManagementRestCRT.class)
class UserManagementRestCRTTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserManagementService uAuthService;

    @Test
    void testNewUser_RegisterSuccess_ReturnsCreatedStatus() throws Exception {
        UserRegisterRequest userRequest = new UserRegisterRequest("mock", "mock", "testPassword");
        UserRegisterResponse registerResponse = new UserRegisterResponse(1);

        when(uAuthService.register(Mockito.any(UserRegisterRequest.class))).thenReturn(registerResponse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRequest));

        MvcResult response = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.CREATED.value(), response.getResponse().getStatus());

        verify(uAuthService, times(1)).register(any(UserRegisterRequest.class));
    }

    @Test
    void testNewUser_RegisterFailure_ReturnsConflictStatus() throws Exception {
        UserRegisterRequest userRequest = new UserRegisterRequest("mock", "mock", "testPassword");

        when(uAuthService.register(any(UserRegisterRequest.class))).thenReturn(null);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRequest));

        MvcResult response =  mockMvc.perform(requestBuilder).andReturn(); 
        
        assertEquals(HttpStatus.CONFLICT.value(), response.getResponse().getStatus());

        verify(uAuthService, times(1)).register(any(UserRegisterRequest.class));
    }

    @Test
    void testGetUsers_ReturnsUsersList() throws Exception {
        UserDTO user1 = new UserDTO();
        user1.setUser_id("1");
        user1.setUsername("user1");
        user1.setName("User 1");
        UserDTO user2 = new UserDTO();
        user2.setUser_id("2");
        user2.setUsername("user2");
        user2.setName("User 2");

        List<UserDTO> users = Arrays.asList(
            user1,
            user2
        );

        when(uAuthService.getUsers()).thenReturn(users);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON);
        

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(uAuthService, times(1)).getUsers();
    }

    @Test
    void testUpdateUser_ValidUser_ReturnsOkStatus() throws Exception {
        UserRegisterRequest userRequest = new UserRegisterRequest("updated", "Updated User", "testPassword");
        String userId = "1";

        when(uAuthService.updateUser(any(UserRegisterRequest.class), Mockito.anyInt())).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/users/{user_id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRequest));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andReturn();
        
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(uAuthService, times(1)).updateUser(any(UserRegisterRequest.class), Mockito.anyInt());
    }

    @Test
    void testUpdateUser_InvalidUser_ReturnsBadRequestStatus() throws Exception {
        UserRegisterRequest userRequest = new UserRegisterRequest("invalid", "Invalid User", "testPassword");
        String userId = "1";

        when(uAuthService.updateUser(any(UserRegisterRequest.class), Mockito.anyInt())).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/users/{user_id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRequest));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        verify(uAuthService, times(1)).updateUser(any(UserRegisterRequest.class), Mockito.anyInt());
    }

    @Test
    void testDeleteUser_ValidUser_ReturnsNoContentStatus() throws Exception {
        String userId = "1";

        when(uAuthService.deleteUser(Mockito.anyInt())).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/{user_id}", userId)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());

        verify(uAuthService, times(1)).deleteUser(Mockito.anyInt());
    }

    @Test
    void testDeleteUser_InvalidUser_ReturnsNotModifiedStatus() throws Exception {
        String userId = "1";

        when(uAuthService.deleteUser(Mockito.anyInt())).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/{user_id}", userId)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.NOT_MODIFIED.value(), result.getResponse().getStatus());

        verify(uAuthService, times(1)).deleteUser(Mockito.anyInt());
    }

    

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}

