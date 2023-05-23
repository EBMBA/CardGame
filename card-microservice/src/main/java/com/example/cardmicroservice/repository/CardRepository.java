package com.example.cardmicroservice.repository;

import com.example.cardmicroservice.model.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CardRepository extends CrudRepository<Card, Integer> {
    public Optional<Card> findByName(String name);
}
