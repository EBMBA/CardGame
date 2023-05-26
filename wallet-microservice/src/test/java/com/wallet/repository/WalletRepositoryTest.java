package com.wallet.repository;

import javax.annotation.Resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wallet.model.Wallet;
import com.wallet.repository.WalletRepository;


@DataJpaTest
class WalletRepositoryTest {
    @Autowired
    WalletRepository wRepository ;

    @AfterEach
    public void cleanUp() {
        wRepository.deleteAll();
    }

    @Test
    void testFindByUserId() {
        Wallet wallet = new Wallet(1, 1000f, 1);
        wRepository.save(wallet);
        Wallet walletE = wRepository.findByUserId(1);
        assert(walletE.getMoney() == 1000f);
    }
   
    @Test
    void testFindByUserIdNull() {
        Wallet wallet = new Wallet(1, 1000f, 1);
        wRepository.save(wallet);
        Wallet walletE = wRepository.findByUserId(2);
        assert(walletE == null);
    }

    @Test
    void saveWallet() {
        Wallet wallet = new Wallet(2, 2000f, 2);
        wRepository.save(wallet);
        assert(wRepository.findByUserId(2).getMoney() == 2000f);
    }

}
