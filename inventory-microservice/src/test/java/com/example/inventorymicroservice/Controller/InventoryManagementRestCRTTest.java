package com.example.inventorymicroservice.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.common.model.InventoryCreationRequest;
import com.example.common.model.InventoryDTO;
import com.example.common.model.InventoryOperationRequest;
import com.example.common.model.UserDTO;
import com.example.common.model.Enum.InventoryTransactionType;
import com.example.inventorymicroservice.controller.InventoryManagementRestCRT;
import com.example.inventorymicroservice.controller.InventoryManagementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InventoryManagementRestCRT.class)
public class InventoryManagementRestCRTTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private InventoryManagementService inventoryManagementService;

    private InventoryDTO inventoryDTO;
    private InventoryCreationRequest inventoryCreationRequest;
    private InventoryOperationRequest inventoryOperationRequest;

    UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO();
        userDTO.setUser_id("1");
        userDTO.setUsername("mock");
        userDTO.setName("mock");
        inventoryDTO = new InventoryDTO();
        inventoryDTO.setUser(userDTO);
        inventoryDTO.setCards(new HashSet<>());

        inventoryCreationRequest = new InventoryCreationRequest();
        inventoryCreationRequest.setUser_id(1);

        inventoryOperationRequest = new InventoryOperationRequest();
        inventoryOperationRequest.setTransaction(InventoryTransactionType.ADD);
        inventoryOperationRequest.setCard_id(1);
    }

    @Test
    public void testGetInventories_InventoryExists_ReturnsOk() throws Exception {
        when(inventoryManagementService.getInventoryDTO(1)).thenReturn(inventoryDTO);

        RequestBuilder request = MockMvcRequestBuilders.get("/api/inventories/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andReturn();
        // expected result     "user": { "user_id": "6",  "username": "d", "name": "test" }, "cards": []
        String expected = "{\"user\":{\"user_id\":\"1\",\"username\":\"mock\",\"name\":\"mock\"},\"cards\":[]}";

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    public void testGetInventories_InventoryDoesNotExist_ReturnsBadRequest() throws Exception {
        when(inventoryManagementService.getInventoryDTO(1)).thenReturn(null);

        RequestBuilder request = MockMvcRequestBuilders.get("/api/inventories/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    public void testAddInventory_InventoryAddedSuccessfully_ReturnsOk() throws Exception {
        when(inventoryManagementService.addInventory(inventoryCreationRequest)).thenReturn(true);
        when(inventoryManagementService.getInventoryDTO(inventoryCreationRequest.getUser_id()))
                .thenReturn(inventoryDTO);

        // String jsonRequest = "{\"user_id\": 1}";

        RequestBuilder request = MockMvcRequestBuilders.post("/api/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inventoryCreationRequest))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testAddInventory_InventoryNotCreated_ReturnsBadRequest() throws Exception {
        when(inventoryManagementService.addInventory(inventoryCreationRequest)).thenReturn(false);

        RequestBuilder request = MockMvcRequestBuilders.post("/api/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inventoryCreationRequest))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    public void testDeleteInventory_InventoryDeletedSuccessfully_ReturnsOk() throws Exception {
        when(inventoryManagementService.deleteInventory(1)).thenReturn(true);

        RequestBuilder request = MockMvcRequestBuilders.delete("/api/inventories/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andReturn();

        assertEquals(HttpStatus.ACCEPTED.value(), result.getResponse().getStatus());
    }

    @Test
    public void testDeleteInventory_InventoryNotDeleted_ReturnsBadRequest() throws Exception {
        when(inventoryManagementService.deleteInventory(1)).thenReturn(false);

        RequestBuilder request = MockMvcRequestBuilders.delete("/api/inventories/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andReturn();

        assertEquals(HttpStatus.NOT_MODIFIED.value(), result.getResponse().getStatus());
    }

    @Test
    public void testUpdateInventory_InventoryUpdatedSuccessfully_ReturnsOk() throws Exception {
        when(inventoryManagementService.addToUserInventory(1,inventoryOperationRequest.getCard_id())).thenReturn(true);

        RequestBuilder request = MockMvcRequestBuilders.put("/api/inventories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inventoryOperationRequest))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andReturn();

        assertEquals(HttpStatus.ACCEPTED.value(), result.getResponse().getStatus());
    }

    @Test
    public void testUpdateInventory_InventoryNotUpdated_ReturnsBadRequest() throws Exception {
        when(inventoryManagementService.addToUserInventory(1,inventoryOperationRequest.getCard_id())).thenReturn(false);

        RequestBuilder request = MockMvcRequestBuilders.put("/api/inventories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inventoryOperationRequest))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request).andReturn();

        assertEquals(HttpStatus.NOT_MODIFIED.value(), result.getResponse().getStatus());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

}

