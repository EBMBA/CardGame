
package com.example.inventorymicroservice.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
public class Inventory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "inventory_generator")
    @SequenceGenerator(name = "inventory_generator", initialValue = 6)
    @Column(name = "inventoryid")
    private Integer inventoryId;

    @Column(name = "userid", nullable = false, unique = true )
    private Integer userId;

    @ElementCollection
    private Set<Integer> cards = new HashSet<>();

    public Boolean hasCard(Integer cardId) {
        return this.cards.contains(cardId);
    }

    public Boolean addCard(Integer cardId){
        return this.cards.add(cardId);
    }

    public Boolean removeCard(Integer cardId){
        return this.cards.remove(cardId);
    }


}

