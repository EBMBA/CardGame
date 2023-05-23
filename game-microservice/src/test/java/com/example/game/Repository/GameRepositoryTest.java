package com.example.game.Repository;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.game.model.Game;
import com.example.game.repository.GameRepository;

@DataJpaTest
class GameRepositoryTest {
    @Autowired
    GameRepository gRepository;

    @AfterEach
    public void cleanUp() {
        gRepository.deleteAll();
    }

    @Test
    void testFindByName() {
        Game game = new Game();
        game.setId(1);
        game.setName("game1");
        game.setMise(100);
        game.setPlayer1(1);
        game.setPlayer2(2);
        game.setPlayer1Card(1);
        game.setPlayer2Card(2);
        game.setStatus("In Progress");
        game.setWinner(1);

        gRepository.save(game);
        Optional<Game> gameE = gRepository.findByName("game1");
        assert(gameE.get().getName() == "game1");
    }

    @Test
    void testFindByNameNull() {
        Game game = new Game();
        game.setId(1);
        game.setName("Test Game");
        game.setMise(100);
        game.setPlayer1(1);
        game.setPlayer2(2);
        game.setPlayer1Card(1);
        game.setPlayer2Card(2);
        game.setStatus("In Progress");
        game.setWinner(1);
        gRepository.save(game);
        Optional<Game> gameE = gRepository.findByName("game2");
        assert(gameE.isEmpty());
    }
}
