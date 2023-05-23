package com.example.common.api;
// This class will be used to communicate with the card microservice
// It will be used to get the card information from the card microservice
// It will be used to update the card information in the card microservice
// It will be used to delete the card information in the card microservice
// It will be used to create the card information in the card microservice
// This class will simplify the communication between the microservices and the card microservice

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.common.model.CardDTO;
import com.example.common.model.UserDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Slf4j
public class CardAPI {
    private final String CARD_SERVICE_URL ;
    private WebClient webClient;


    public CardAPI(String cardServiceUrl) {
        this.CARD_SERVICE_URL = cardServiceUrl;
        this.webClient = WebClient.create(CARD_SERVICE_URL);
    }

    public CardAPI() {
        this.CARD_SERVICE_URL = "http://localhost:8088/api/card";
        this.webClient = WebClient.create(CARD_SERVICE_URL);
    }

    public void setCARD_SERVICE_URL(String cardServiceUrl) {
        this.webClient = WebClient.create(cardServiceUrl);
    }

    // This method will return the card with the given card id with the use of webclient
    public CardDTO getCard(Integer cardid) {
        CardDTO cardDTO = webClient.get()
            .uri(CARD_SERVICE_URL  + "/" + cardid)
            .retrieve()
            .bodyToMono(CardDTO.class)
            .block();

        log.info("CardDTO get from request to the card microservice: {}", cardDTO);
        return cardDTO;
    }

    // This method will update the card with the given card id with the use of webclient
    // Request return OK if the wallet is updated and NOT_MODIFIED if the wallet is not updated
    public Boolean updateCard(Integer cardid, CardDTO cardDTO) {
        ResponseEntity<Void> result = webClient.put()
            .uri(CARD_SERVICE_URL  + "/" + cardid)
            .bodyValue(cardDTO)
            .retrieve()
            .toBodilessEntity()
            .block();

        if (result == null || result.getStatusCode().isError()) {
            log.error("CardDTO update from request to the card microservice failed");
            return false;
        }

        log.info("CardDTO update from request to the card microservice: {}", result);
        return result.getStatusCode().is2xxSuccessful();
    }

    // This method will delete the card with the given card id with the use of webclient
    // Request return OK if the wallet is deleted and NOT_MODIFIED if the wallet is not deleted
    public Boolean deleteCard(Integer cardid) {
        ResponseEntity<Void> result = webClient.delete()
            .uri(CARD_SERVICE_URL  + "/" + cardid)
            .retrieve()
            .toBodilessEntity()
            .block();

        if (result == null || result.getStatusCode().is3xxRedirection()) {
            log.error("CardDTO delete from request to the card microservice failed");
            return false;
        }

        log.info("CardDTO delete from request to the card microservice: {}", result);
        return result.getStatusCode().is2xxSuccessful();
    }

    // This method will create the card with the given card id with the use of webclient
    // Request return OK if the wallet is created and BAD_REQUEST if the wallet is not created 
    public Boolean createCard(CardDTO cardDTO) {
        ResponseEntity<Void> result = webClient.post()
            .uri(CARD_SERVICE_URL)
            .bodyValue(cardDTO)
            .retrieve()
            .toBodilessEntity()
            .block();

        if (result == null || result.getStatusCode().isError()) {
            log.error("CardDTO create from request to the card microservice failed");
            return false;
        }

        log.info("CardDTO create from request to the card microservice: {}", result);
        return result.getStatusCode().is2xxSuccessful();
    }

    // This method will return a random card id (/random) with the use of webclient
    public Integer getRandomCardId() {
        Integer cardid = webClient.get()
            .uri(CARD_SERVICE_URL  + "/random")
            .retrieve()
            .bodyToMono(Integer.class)
            .block();

        log.info("CardDTO get random from request to the card microservice: {}", cardid);
        return cardid;
    }
    
    // This method will return all the cards with the use of webclient
    public List<CardDTO> getAllCards() {
        List<CardDTO> cardDTOs = webClient.get()
        .uri(CARD_SERVICE_URL)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<CardDTO>>() {})
        .onErrorResume(throwable -> Mono.just(Collections.emptyList()))
        .block();

        log.info("CardDTO get all from request to the card microservice: {}", cardDTOs);
        return cardDTOs;
    }
}
