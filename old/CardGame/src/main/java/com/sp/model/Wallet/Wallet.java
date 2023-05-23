package com.sp.model.Wallet;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sp.model.User.User;

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

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    public Boolean hasEnoughMoney(Float money) {
        return this.money - money >= 0;
    }
}
