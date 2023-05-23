package com.example.usermicroservice.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter  
@Entity
@Table(name = "users")
public class User implements Serializable{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", initialValue = 6)
    private Integer userId;
    private String username;
    private String name;
    
    
    @Override
    public String toString() {
		return "USER ["+this.userId+"]: name:"+this.name+", username:"+this.username;
    }
}
