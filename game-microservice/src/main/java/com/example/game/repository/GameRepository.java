package com.example.game.repository;

import com.example.game.model.Game;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, Integer>{

    //Iterable<Object> findAll();
    public Optional<Game> findByName(String name);
}
