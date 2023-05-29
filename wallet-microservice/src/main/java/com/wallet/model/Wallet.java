package com.wallet.model;

import java.io.Serializable;
import java.util.function.Supplier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.example.common.Exception.WalletNotFoundException;
import com.example.common.model.UserDTO;
import com.example.common.model.WalletDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "wallets")
public class Wallet implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "wallet_generator")
    @SequenceGenerator(name = "wallet_generator", initialValue = 6)
    private Integer wallet_id;
    private Float money;

    // @OneToOne(fetch = FetchType.EAGER, optional = false)
    // @JoinColumn(name = "user_id", nullable = false)
    @Column(unique = true, nullable = false)
    private Integer userId;
    // Anotation unqiue in database 
  
    
    public Boolean hasEnoughMoney(Float money) {
        return this.money + money >= 0;
    }


    public Wallet orElseThrow(Supplier<WalletNotFoundException> of) {
        return null;
    }
}
