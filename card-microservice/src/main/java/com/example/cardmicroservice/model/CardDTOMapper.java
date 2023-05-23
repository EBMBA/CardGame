package com.example.cardmicroservice.model;


import com.example.common.model.CardDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CardDTOMapper implements Function<Card, CardDTO> {
    @Override
    public CardDTO apply(Card card) {
        return new CardDTO(
                card.getCardId().toString(),
                card.getName(),
                card.getDescription(),
                card.getImgUrl(),
                card.getFamily(),
                card.getAffinity(),
                card.getHp(),
                card.getEnergy(),
                card.getAttack(),
                card.getDefense(),
                card.getPrice()
        );
    }
}
