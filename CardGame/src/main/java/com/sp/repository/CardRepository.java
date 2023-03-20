package com.sp.repository;

import com.sp.model.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public interface CardRepository extends CrudRepository<Card, Integer> {
}
