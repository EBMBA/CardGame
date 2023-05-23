package com.example.cardmicroservice.model;


import javax.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "card_generator")
    @SequenceGenerator(name = "card_generator", initialValue = 151)
    private Integer cardId;

    private String name;
    @Column(length = 50000)
    private String description;
    private String imgUrl;
    private String family;
    private String affinity;
    private String hp;
    private String energy;
    private String attack;
    private String defense;
    private Float  price;

    // Annotation to avoid infinite loop
    /*@ManyToMany(mappedBy = "cards")
    @JsonIgnore
    @ToString.Exclude
    private Set<Inventory> inventories = new HashSet<>();
    */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
    	return "Card ["+this.cardId+"]: name:"+this.name+", description:"+this.description+", imgUrl:"+this.imgUrl+" family:"+this.family+" affinity:"+this.affinity+" hp:"+this.hp+" energy:"+this.energy+" attack:"+this.attack+" defense:"+this.defense+" price:"+this.price;//+" users who have this card:"+ this.users;
    }
}
