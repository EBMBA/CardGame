package com.sp.model.Card;

import java.util.function.Function;

import org.springframework.stereotype.Service;

@Service
public class CardDTOMapper implements Function<Card, CardDTO> {
    @Override
    public CardDTO apply(Card card) {
        return new CardDTO(
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
