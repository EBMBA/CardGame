package com.sp.controller;

import java.util.function.BiFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.model.Card.Card;
import com.sp.model.Store.StoreOperationRequest;
import com.sp.model.Store.TransactionType;
import com.sp.model.User.User;

@Service
public class StoreService {
    @Autowired
    InventoryManagementService inventoryManagementService;

    @Autowired
    UserManagementService userManagementService;

    @Autowired
    CardManagementService cardManagementService;

    @Autowired
    WalletManagementService walletManagementService;

    public Boolean doTransactionCard(StoreOperationRequest storeOperationRequest){
        Card card = cardManagementService.getCardById(Integer.valueOf(storeOperationRequest.getCard_id()));
        User user = userManagementService.getUser(storeOperationRequest.getUsername());

        // Check if user exist 
        if (user == null) {
            System.err.println("UserNotFound"); 
            return false;
        }

        // Check if card exist
        if (card == null) {
            System.err.println("CardNotFound"); 
            return false;            
        }

        // Do the money transaction
        Float walletTransactionAmount= storeOperationRequest.getTransaction() == TransactionType.BUY ? -card.getPrice() : card.getPrice(); 
        if (!walletManagementService.doTransaction(user, walletTransactionAmount)) {
            System.err.println("TransactionImpossible"); 
            return false;
        }

        // Add or Remove the card depending on the transaction 
        BiFunction<User, Card, Boolean> inventoryFunction = storeOperationRequest.getTransaction() == TransactionType.BUY ? 
                                                                    (object1, object2) -> inventoryManagementService.addToUserInventory(object1, object2) : 
                                                                    (object1, object2) -> inventoryManagementService.removeFromUserInventory(object1, object2); 
        if(!inventoryFunction.apply(user, card)){
            System.err.println("ErrorAddingCard");
            walletManagementService.doTransaction(user, -1*walletTransactionAmount);
            return false;
        }

        return true;
    }

}
