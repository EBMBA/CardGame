package com.sp.model.Inventory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sp.model.Card.Card;
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
@Table(name = "inventory")
public class Inventory implements Serializable{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "inventory_generator")
    @SequenceGenerator(name = "inventory_generator", initialValue = 6)
    @Column(name = "inventory_id")
    private Integer inventoryId;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(name = "user_cards",
    joinColumns = { @JoinColumn(name = "inventory_id") },
    inverseJoinColumns = { @JoinColumn(name = "cards_id") })
    private Set<Card> cards = new HashSet<>();

    public Boolean hasCard(Card card) {
        return this.cards.contains(card);
    }
    public Boolean hasCard(Integer id) {
        for (Card card : this.cards) {
            if (card.getCardId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public Boolean addCard(Card card){
        return this.cards.add(card);
    }

    public Boolean removeCard(Card card){
        return this.cards.remove(card);
    }
}
