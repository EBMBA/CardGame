package com.example.common.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.common.model.InventoryDTO;
import com.example.common.model.InventoryOperationRequest;
import com.example.common.model.InventoryCreationRequest;

import lombok.extern.slf4j.Slf4j;

// This class will be used to communicate with the inventory microservice
// It will be used to get the inventory information from the inventory microservice
// It will be used to update the inventory information in the inventory microservice
// It will be used to delete the inventory information in the inventory microservice
// It will be used to create the inventory information in the inventory microservice
// This class will simplify the communication between the microservices and the inventory microservice
// Generate the JAVA doc for this class

@Slf4j
public class InventoryAPI {
    private String INVENTORY_SERVICE_URL = "http://localhost:8088/api/inventories" ;
    private WebClient webClient;

    public InventoryAPI(@Value("${INVENTORY_SERVICE_URL}") String inventoryServiceUrl) {
        this.INVENTORY_SERVICE_URL = inventoryServiceUrl;
        this.webClient = WebClient.create(INVENTORY_SERVICE_URL);
    }

    public InventoryAPI() {
    }

    public void setINVENTORY_SERVICE_URL(String inventoryServiceUrl) {
        this.INVENTORY_SERVICE_URL = inventoryServiceUrl;
        this.webClient = WebClient.create(INVENTORY_SERVICE_URL);
    }

    // This method will return the inventory with the given user id with the use of webclient
    // Request return OK if the inventory is found and BAD_REQUEST if the inventory is not found
    public InventoryDTO getInvetory(Integer user_id) {
        InventoryDTO inventoryDTO = webClient.get()
            .uri(INVENTORY_SERVICE_URL  + "/" + user_id)
            .retrieve()
            .bodyToMono(InventoryDTO.class)
            .block();

        if (inventoryDTO == null) {
            log.error("No inventory found for user_id: {}", user_id);
            return null;
        }

        log.info("InventoryDTO get from request to the inventory microservice: {}", inventoryDTO);
        return inventoryDTO;
    }

    // This method will update the inventory with the given user id and the InventoryOperationRequest with the use of webclient      
    // Request return ACCEPTED if the inventory is updated and NOT_MODIFIED if the inventory is not updated
    public Boolean updateInventory(Integer user_id, InventoryOperationRequest inventoryOperationRequest) {
        ResponseEntity<Void> responseEntity = webClient.put()
            .uri(INVENTORY_SERVICE_URL + "/" + user_id)
            .bodyValue(inventoryOperationRequest)
            .retrieve()
            .toBodilessEntity()
            .block();

        if (responseEntity == null || responseEntity.getStatusCode() == HttpStatus.NOT_MODIFIED) {
            log.error("Inventory not updated for user_id: {}", user_id);
            return false;
        }

        log.info("Inventory updated for user_id: {}", user_id);
        return responseEntity.getStatusCode()  == HttpStatus.ACCEPTED;
    }

    // This method will delete the inventory with the given user id with the use of webclient
    // Request return ACCEPTED if the inventory is deleted and NOT_MODIFIED if the inventory is not deleted
    public Boolean deleteInventory(Integer user_id) {
        ResponseEntity<Void> responseEntity = webClient.delete()
            .uri(INVENTORY_SERVICE_URL + "/" + user_id)
            .retrieve()
            .toBodilessEntity()
            .block();

        if (responseEntity == null || responseEntity.getStatusCode() == HttpStatus.NOT_MODIFIED) {
            log.error("Inventory not deleted for user_id: {}", user_id);
            return false;
        }

        log.info("Inventory deleted for user_id: {}", user_id);
        return responseEntity.getStatusCode()  == HttpStatus.ACCEPTED;
    }

    // This method will create the inventory with the given InventoryCreationRequest with the use of webclient
    // Request return OK if the inventory is created and BAD_REQUEST if the inventory is not created
    public Boolean createInventory(InventoryCreationRequest inventoryCreationRequest) {
        ResponseEntity<Void> responseEntity = webClient.post()
            .uri(INVENTORY_SERVICE_URL)
            .bodyValue(inventoryCreationRequest)
            .retrieve()
            .toBodilessEntity()
            .block();
        
        if (responseEntity == null || responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            log.error("Inventory not created for user_id: {}", inventoryCreationRequest.getUser_id());
            return false;
        }

        log.info("Inventory created for user_id: {}", inventoryCreationRequest.getUser_id());
        return responseEntity.getStatusCode() == HttpStatus.OK;
    }
    
}
