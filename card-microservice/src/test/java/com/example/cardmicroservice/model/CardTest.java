package com.example.cardmicroservice.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.cardmicroservice.model.Card;

class CardTest {
    private List<Integer> cardId;
    private List<String> name;
    private List<String> description;
    private List<String> imgUrl;
    private List<String> family;
    private List<String> affinity;
    private List<String> hp;
    private List<String> energy;
    private List<String> attack;
    private List<String> defense;
    private List<Float>  price;

    @BeforeEach
    public void setUp() {
        System.out.println("[BEFORE TEST] -- Add card to test");
        cardId = new ArrayList<Integer>();
        name = new ArrayList<String>();
        description = new ArrayList<String>();
        imgUrl = new ArrayList<String>();
        family = new ArrayList<String>();
        affinity = new ArrayList<String>();
        hp = new ArrayList<String>();
        energy = new ArrayList<String>();
        attack = new ArrayList<String>();
        defense = new ArrayList<String>();
        price = new ArrayList<Float>();
        
        // Correct input 
        cardId.add(1);
        name.add("name");
        description.add("description");
        imgUrl.add("imgUrl");
        family.add("family");
        affinity.add("affinity");
        hp.add("hp");
        energy.add("energy");
        attack.add("attack");
        defense.add("defense");
        price.add(1.0f);

        // Incorrect input
        cardId.add(-1);
        name.add(";:!;!::!;;<>");
        description.add(";:!;!::!;;<>");
        imgUrl.add(";:!;!::!;;<>");
        family.add(";:!;!::!;;<>");
        affinity.add(";:!;!::!;;<>");
        hp.add(";:!;!::!;;<>");
        energy.add(";:!;!::!;;<>");
        attack.add(";:!;!::!;;<>");
        defense.add(";:!;!::!;;<>");
        price.add(-1.0f);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("[AFTER TEST] -- Remove card to test");
        cardId = null;
        name = null;
        description = null;
        imgUrl = null;
        family = null;
        affinity = null;
        hp = null;
        energy = null;
        attack = null;
        defense = null;
        price = null;
    }

    @Test
    void testCard() {
        System.out.println("[TEST] -- Card creation");
        for (int i = 0; i < cardId.size(); i++) {
            Card card = new Card(cardId.get(i), name.get(i), description.get(i), imgUrl.get(i), family.get(i), affinity.get(i), hp.get(i), energy.get(i), attack.get(i), defense.get(i), price.get(i));
            System.out.println(card);
            assertTrue(card.getCardId() == cardId.get(i));
            assertTrue(card.getName().equals(name.get(i)));
            assertTrue(card.getDescription().equals(description.get(i)));
            assertTrue(card.getImgUrl().equals(imgUrl.get(i)));
            assertTrue(card.getFamily().equals(family.get(i)));
            assertTrue(card.getAffinity().equals(affinity.get(i)));
            assertTrue(card.getHp().equals(hp.get(i)));
            assertTrue(card.getEnergy().equals(energy.get(i)));
            assertTrue(card.getAttack().equals(attack.get(i)));
            assertTrue(card.getDefense().equals(defense.get(i)));
            assertTrue(card.getPrice() == price.get(i));
        }
    }

    // Test toString method of Card
    // exepected result : "Card ["+this.cardId+"]: name:"+this.name+", description:"+this.description+", imgUrl:"+this.imgUrl+" family:"+this.family+" affinity:"+this.affinity+" hp:"+this.hp+" energy:"+this.energy+" attack:"+this.attack+" defense:"+this.defense+" price:"+this.price
    @Test
    void testToString() {
        System.out.println("[TEST] -- Card toString");
        for (int i = 0; i < cardId.size(); i++) {
            Card card = new Card(cardId.get(i), name.get(i), description.get(i), imgUrl.get(i), family.get(i), affinity.get(i), hp.get(i), energy.get(i), attack.get(i), defense.get(i), price.get(i));
            System.out.println(card);
            assertTrue(card.toString().equals("Card ["+cardId.get(i)+"]: name:"+name.get(i)+", description:"+description.get(i)+", imgUrl:"+imgUrl.get(i)+" family:"+family.get(i)+" affinity:"+affinity.get(i)+" hp:"+hp.get(i)+" energy:"+energy.get(i)+" attack:"+attack.get(i)+" defense:"+defense.get(i)+" price:"+price.get(i)));
        }
    }
    
}
