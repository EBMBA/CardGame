package com.sp.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cardId;

	private String name;
	@Size(max = 5000)
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
	@JsonIgnore
	@ManyToMany(mappedBy = "cards")
    private Set<User> users = new HashSet<>();
	
	public Card() {
		this.name = "";
		this.description = "";
		this.imgUrl = "";
		this.family = "";
		this.affinity = "";
		this.hp = "";
		this.energy = "";
		this.attack = "";
		this.defense = "";
		this.price = null;
	}

	public Card(String name, String description, String imgUrl, String family, String affinity, String hp, String energy, String attack, String defense, Float price) {
		this.name = name;
		this.description = description;
		this.imgUrl = imgUrl;
		this.family = family;
		this.affinity = affinity;
		this.hp = hp;
		this.energy = energy;
		this.attack = attack;
		this.defense = defense;
		this.price = price;
	}
	
	public Set<User> getUsers() {
		return this.users;
	}
	
	public void setUsers(Set<User> users) {
		this.users = users;
	}  

	public Integer getCardId() {
		return cardId;
	}

	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public String getFamily() {
		return family;
	}
	public String getAffinity() {
		return affinity;
	}
	public String getHp() {
		return hp;
	}
	public String getEnergy() {
		return energy;
	}
	public String getAttack() {
		return attack;
	}
	public String getDefense() {
		return defense;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getPrice(){
		return price;
	}
	public void setPrice(Float price){
		this.price = price;
	}
	@Override
    public String toString() {
        // TODO Auto-generated method stub
		return "Card ["+this.cardId+"]: name:"+this.name+", description:"+this.description+", imgUrl:"+this.imgUrl+" family:"+this.family+" affinity:"+this.affinity+" hp:"+this.hp+" energy:"+this.energy+" attack:"+this.attack+" defense:"+this.defense+" price:"+this.price;//+" users who have this card:"+ this.users;
    }
}
