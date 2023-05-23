package com.example.game.Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.game.model.Game;

class GameTest {
    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    @Test
    void testGameInitialization() {
        Assertions.assertNull(game.getId());
        Assertions.assertNull(game.getName());
        Assertions.assertNull(game.getMise());
        Assertions.assertNull(game.getPlayer1());
        Assertions.assertNull(game.getPlayer2());
        Assertions.assertNull(game.getPlayer1Card());
        Assertions.assertNull(game.getPlayer2Card());
        Assertions.assertNull(game.getStatus());
        Assertions.assertNull(game.getWinner());
    }

    @Test
    void testGameSetterGetter() {
        game.setId(1);
        game.setName("Test Game");
        game.setMise(100);
        game.setPlayer1(1);
        game.setPlayer2(2);
        game.setPlayer1Card(1);
        game.setPlayer2Card(2);
        game.setStatus("In Progress");
        game.setWinner(1);

        Assertions.assertEquals(1, game.getId());
        Assertions.assertEquals("Test Game", game.getName());
        Assertions.assertEquals(100, game.getMise());
        Assertions.assertEquals(1, game.getPlayer1());
        Assertions.assertEquals(2, game.getPlayer2());
        Assertions.assertEquals(1, game.getPlayer1Card());
        Assertions.assertEquals(2, game.getPlayer2Card());
        Assertions.assertEquals("In Progress", game.getStatus());
        Assertions.assertEquals(1, game.getWinner());
    }
}
