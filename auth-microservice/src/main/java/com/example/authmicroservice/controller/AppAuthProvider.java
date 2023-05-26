package com.example.authmicroservice.controller ;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

/**
 * AppAuthProvider
 */
public class AppAuthProvider extends DaoAuthenticationProvider  {
    @Autowired
    AuthService uService;

    // THis is the method that is called when the user tries to login
    // It checks if the user exists and if the password matches the one in the database (encoded)
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
        String name = auth.getName();
        String password = auth.getCredentials().toString();
        
        UserDetails user;

        try {
            user = uService.loadUserByUsername(name);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }

        // UserDetails user = uService.loadUserByUsername(name);        
        if (user == null) {
            throw new BadCredentialsException("Username/Password does notmatch for " + auth.getPrincipal() );
        }
        else if(!this.getPasswordEncoder().matches(password, user.getPassword())){
            System.out.println(user.getPassword() +" vs "+ this.getPasswordEncoder().encode(password));
            throw new BadCredentialsException("Username/Password does not match for " + auth.getPrincipal() );
        }
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
