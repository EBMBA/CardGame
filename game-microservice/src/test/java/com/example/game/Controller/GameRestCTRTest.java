package com.example.game.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import com.example.game.controller.GameRestCRT;
import com.example.game.controller.GameService;
import com.example.game.model.GameDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameRestCRT.class)
public class GameRestCTRTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // test createRoom
    @Test
    public void testCreateRoom() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("room_name", "Test Room");
        requestBody.put("mise", "100");

        when(gameService.createRoom(anyString(), anyInt(), anyString())).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/game/room")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(requestBody));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("Room created", result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateRoom_RoomAlreadyExists() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("room_name", "Test Room");
        requestBody.put("mise", "100");

        when(gameService.createRoom(anyString(), anyInt(), anyString())).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/game/room")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(requestBody));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Room already exists", result.getResponse().getContentAsString());  
    }    

    // test getRooms
    @Test
    public void testGetRooms() throws Exception {
        GameDTO room = new GameDTO();
        room.setName("room1");
        GameDTO room2 = new GameDTO();
        room2.setName("room2");

        List<GameDTO> rooms = Arrays.asList(
            room,
            room2
        );

        when(gameService.getRooms()).thenReturn(rooms);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/game/rooms");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(rooms), result.getResponse().getContentAsString());
    }

    @Test
    public void testGetRooms_noRooms() throws Exception {
        when(gameService.getRooms()).thenReturn(null);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/game/rooms");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("No rooms", result.getResponse().getContentAsString());
    }

    // test joinRoom
    @Test
    public void testJoinRoom() throws Exception {
        String roomName = "Room 1";
        String jwtToken = "mockToken";

        when(gameService.joinRoom(roomName, jwtToken)).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/game/join/{room_name}", roomName)
                .header("Authorization", "Bearer " + jwtToken);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("Room joined", result.getResponse().getContentAsString());
    }

    @Test
    public void testJoinRoom_RoomNotFoundOrAlreadyPlaying() throws Exception {
        String roomName = "NonExistingRoom";
        String jwtToken = "mockToken";
       
        when(gameService.joinRoom(roomName, jwtToken)).thenReturn(false);


        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/game/join/{room_name}", roomName)
                .header("Authorization", "Bearer " + jwtToken);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Room not found or already playing", result.getResponse().getContentAsString());
    }
    
    // test updateRoom
    @Test
    public void testUpdateRoom_Success() throws Exception {
        String roomName = "mockRoom";
        String jwtToken = "mockToken";
        int cardId = 123;
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("card_id", Integer.toString(cardId));
        when(gameService.updateRoom(roomName, jwtToken, cardId)).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/game/room/{room_name}", roomName)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(requestBody));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("Room updated", result.getResponse().getContentAsString());
    }

    @Test
    public void testUpdateRoom_ErrorUpdatingRoom() throws Exception {
        // Arrange
        String roomName = "exampleRoom";
        String jwtToken = "exampleToken";
        int cardId = 123;
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("card_id", Integer.toString(cardId));
        when(gameService.updateRoom(roomName, jwtToken, cardId)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/game/room/{room_name}", roomName)
        .header("Authorization", "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(requestBody));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Error updating room", result.getResponse().getContentAsString());
    }

    // test getRoom
    @Test
    public void testGetRoom_Success() throws Exception {
        // Arrange
        String roomName = "exampleRoom";
        GameDTO room = new GameDTO();
        room.setName(roomName);
        
        
        when(gameService.getRoom(roomName)).thenReturn(room);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/game/room/{room_name}", roomName);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(room), result.getResponse().getContentAsString());
    }

    @Test
    public void testGetRoom_RoomNotFound() throws Exception {
        // Arrange
        String roomName = "nonExistentRoom";
        when(gameService.getRoom(roomName)).thenReturn(null);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/game/room/{room_name}", roomName);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Room not found", result.getResponse().getContentAsString());    
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
