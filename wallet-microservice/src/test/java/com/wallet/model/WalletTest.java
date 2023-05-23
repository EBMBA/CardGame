package com.wallet.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.wallet.model.Wallet.Wallet;

class WalletTest {
    private List<Integer> walletIds;
    private List<Float> money;
    private List<Integer> userIds;

    @BeforeEach
    void setUp() {
        walletIds = List.of(1, 2, 3);
        money = List.of(1000f, 2000f, 3000f);
        userIds = List.of(1, 2, 3);
    }

    @AfterEach
    void tearDown() {
        walletIds = null;
        money = null;
        userIds = null;
    }

    @Test
    void testWallet() {
        for (int i = 0; i < walletIds.size(); i++) {
            Wallet wallet = new Wallet(walletIds.get(i), money.get(i), userIds.get(i));
            assertTrue(walletIds.get(i).intValue() == wallet.getWallet_id().intValue());
            assertTrue(money.get(i) == wallet.getMoney());
            assertTrue(userIds.get(i) == wallet.getUserId());
        }
    }
}
