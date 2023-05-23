package com.sp.repository;

import com.sp.model.Card.Card;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;


public interface CardRepository extends CrudRepository<Card, Integer> {
    public Optional<Card> findByName(String name);
}
