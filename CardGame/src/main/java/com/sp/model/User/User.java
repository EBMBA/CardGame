package com.sp.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sp.model.Role;
import com.sp.model.Wallet.Wallet;

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
public class User implements UserDetails{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", initialValue = 6)
    private Integer userId;
    private String username;
    private String name;
    private String password;
    private Float money;

    // @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ManyToMany(fetch = FetchType.EAGER,
    cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(name = "USER_ROLE",
                joinColumns = {@JoinColumn(name= "USER_ID")},
                inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    private List<Role> roleList;
   
    @OneToOne(mappedBy= "user", fetch = FetchType.EAGER,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        })
    private Wallet wallet;
    
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
        List<SimpleGrantedAuthority> roleListAuthorities = new ArrayList<>();
        for (Role role : roleList) {
            roleListAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return roleListAuthorities;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
		return "HERO ["+this.userId+"]: name:"+this.name+", username:"+this.username+", password:"+this.password;
    }
}
