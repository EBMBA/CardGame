package com.sp.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements Serializable, UserDetails{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String username;
    private String name;
    private String password;
    private Float money;

  @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(name = "user_cards",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "cards_id") })
    private Set<Card> cards = new HashSet<>();
   
    public User() {
    }

    public User(Integer userId, String username, String name, String password ){
        super();
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public Boolean addCard(Card card){
        Float moneyLeft = this.money - card.getPrice();
        if (moneyLeft < 0 ) {
            return false;
        }
        this.cards.add(card);
        this.setMoney(moneyLeft); 
        return true;
    }

    public void removeCard(Card card){
        Float moneyLeft = this.money + card.getPrice();
        this.cards.remove(card);
        this.setMoney(moneyLeft); 
    }

    public Set<Card> getCards(){
        return this.cards;
    }

    public Integer getId() {
        return userId;
    }
    public Float getMoney() {
        return money;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
    public void setMoney(Float money) {
        this.money = money;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
		return "HERO ["+this.userId+"]: name:"+this.name+", username:"+this.username+", money:"+this.money+" password:"+this.password+" user's cards:"+this.cards;
    }
}
