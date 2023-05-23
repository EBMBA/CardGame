package com.example.authmicroservice.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter  
@Entity
public class AuthUser implements Serializable, UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "auth_generator")
    @SequenceGenerator(name = "auth_generator", initialValue = 6)
    private Integer authId;

    @Column(unique = true)
    private Integer userId;

    @Column(unique = true)
    private String username;
    private String password;
    
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

    @Override
    public String toString() {
		return "User " + this.authId +" ["+this.userId+"]" + ": username:"+this.username+", password:"+this.password;
    }
}
