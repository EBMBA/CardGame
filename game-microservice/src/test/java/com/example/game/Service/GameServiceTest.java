package com.example.game.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.common.api.AuthAPI;
import com.example.common.api.CardAPI;
import com.example.common.api.WalletAPI;
import com.example.game.controller.GameService;
import com.example.game.model.Game;
import com.example.game.model.GameDTO;
import com.example.game.repository.GameRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameService.class)
class GameServiceTest {

    @MockBean
    private GameRepository mygameRepository;

    @Mock
    private AuthAPI authAPI;

    @Mock
    private CardAPI cardAPI;

    @Mock
    private WalletAPI walletAPI;

    @Autowired
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameService.setAuthAPI(authAPI);
        gameService.setCardAPI(cardAPI);
        gameService.setWalletAPI(walletAPI);
    }

    @Test
    void createRoom_roomDoesNotExist_returnsTrue() {
        String roomName = "room211";
        int mise = 100;
        String token = "token123";

        when(mygameRepository.findByName(roomName)).thenReturn(Optional.empty());
        when(gameService.getIdFromToken(token)).thenReturn(123);

        boolean result = gameService.createRoom(roomName, mise, token);

        assertTrue(result);
        verify(mygameRepository, times(1)).findByName(roomName);
        verify(mygameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void createRoom_roomAlreadyExists_returnsFalse() {
        String roomName = "room1";
        int mise = 100;
        String token = "token123";
        Game existingGame = new Game();

        when(mygameRepository.findByName(roomName)).thenReturn(Optional.of(existingGame));

        boolean result = gameService.createRoom(roomName, mise, token);

        assertFalse(result);
        verify(mygameRepository, times(1)).findByName(roomName);
        verify(mygameRepository, times(0)).save(any(Game.class));
    }

    // test getRoom
    @Test
    void getRoom_roomExists_returnsRoom() {
        String roomName = "room1";
        Game existingGame = new Game();
        GameDTO existingGameDTO = new GameDTO();

        when(mygameRepository.findByName(roomName)).thenReturn(Optional.of(existingGame));

        GameDTO result = gameService.getRoom(roomName);

        assertEquals(existingGameDTO.getName(), result.getName());
        verify(mygameRepository, times(1)).findByName(roomName);
    }

    @Test
    void getRoom_roomDoesNotExist_returnsNull() {
        String roomName = "room1";

        when(mygameRepository.findByName(roomName)).thenReturn(Optional.empty());

        GameDTO result = gameService.getRoom(roomName);

        assertNull(result);
        verify(mygameRepository, times(1)).findByName(roomName);
    }

    // test getRooms
    @Test
    void getRooms_noRooms_returnsEmptyList() {
        Iterable<Game> listeGameDTO = new ArrayList<>();
        when(mygameRepository.findAll()).thenReturn(listeGameDTO);

        assertEquals(0, gameService.getRooms().size());
        verify(mygameRepository, times(1)).findAll();
    }

    @Test
    void getRooms_roomsExist_returnsRooms() {
        Game existingGame = new Game();
        when(mygameRepository.findAll()).thenReturn(java.util.Arrays.asList(existingGame));

        assertEquals(1, gameService.getRooms().size());
        verify(mygameRepository, times(1)).findAll();
    }

    // test joinRoom
    @Test
    void testJoinRoom_noRoom_returnFalse(){
        when(mygameRepository.findByName("room1")).thenReturn(Optional.empty());

        assertFalse(gameService.joinRoom("room1", "token123"));
    }

    @Test
    void testJoinRoom_roomExist_returnTrue(){
        Game existingGame = new Game();
        existingGame.setName("room1");
        existingGame.setStatus("WAIT");
        when(mygameRepository.findByName("room1")).thenReturn(Optional.of(existingGame));

        assertTrue(gameService.joinRoom("room1", "token123"));
    }

    // test updateRoom
    @Test
    void testUpdateRoom_roomExist_returnTrue(){
        Game existingGame = new Game();
        existingGame.setName("room1");
        existingGame.setStatus("PLAY");
        existingGame.setPlayer1(1);
        existingGame.setPlayer2(2);
        existingGame.setPlayer1Card(1);
        existingGame.setPlayer2Card(null);
        when(mygameRepository.findByName("room1")).thenReturn(Optional.of(existingGame));
        when(authAPI.getUserIdFromToken("token123")).thenReturn(2);

        assertTrue(gameService.updateRoom("room1", "token123",1));
    }

    @Test
    void testUpdateRoom_noRoom_returnFalse(){
        Game existingGame = new Game();
        existingGame.setName("room1");
        existingGame.setStatus("PLAY");
        existingGame.setPlayer1(1);
        existingGame.setPlayer2(2);
        existingGame.setPlayer1Card(1);
        existingGame.setPlayer2Card(null);
        when(mygameRepository.findByName("room1")).thenReturn(Optional.empty());

        assertFalse(gameService.updateRoom("room1", "token123",1));
    }

    @Test
    void testUpdateRoom_roomExist_PlayerNotInTheGame_returnTrue(){
        Game existingGame = new Game();
        existingGame.setName("room1");
        existingGame.setStatus("PLAY");
        existingGame.setPlayer1(1);
        existingGame.setPlayer2(2);
        existingGame.setPlayer1Card(1);
        existingGame.setPlayer2Card(null);
        when(mygameRepository.findByName("room1")).thenReturn(Optional.of(existingGame));
        when(authAPI.getUserIdFromToken("token123")).thenReturn(3);

        assertFalse(gameService.updateRoom("room1", "token123",1));
    }
}

