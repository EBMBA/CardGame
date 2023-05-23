package com.example.authmicroservice.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.example.authmicroservice.config.RestTemplateConfig;
import com.example.authmicroservice.controller.AppAuthProvider;
import com.example.authmicroservice.controller.AuthService;
import com.example.authmicroservice.controller.JwtAuthEntryPoint;
import com.example.authmicroservice.controller.JwtRequestFilter;



@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService userDetailsService;

    // This is the constructor for the SecurityConfig class
    public SecurityConfig(JwtAuthEntryPoint jwtAuthenticationEntryPoint,
                          JwtRequestFilter jwtRequestFilter,
                          PasswordEncoder passwordEncoder,
                          AuthService userDetailsService
                        ) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }


    // This method will create a bean for the AuthenticationManager
    // We will use the AuthenticationConfiguration class to create the AuthenticationManager bean
    // We will return the AuthenticationManager bean
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception{
        return authConfiguration.getAuthenticationManager();
    }

    // This method will create a bean for the AuthenticationProvider
    // We will use the AppAuthProvider class to create the AuthenticationProvider bean
    // We will set the userDetailsService and the passwordEncoder for the AppAuthProvider bean
    // We will return the AppAuthProvider bean
    @Bean
    public AuthenticationProvider getProvider(){
        AppAuthProvider provider = new  AppAuthProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // This method will create a bean for the SecurityFilterChain
    // We will use the HttpSecurity class to create the SecurityFilterChain bean
    // We will disable csrf and set the authenticationEntryPoint and sessionCreationPolicy for the HttpSecurity bean
    // We will set the authenticationProvider and authorizeRequests for the HttpSecurity bean
    // We will add a filter to validate the tokens with every request
    // We will return the SecurityFilterChain bean
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http   
                    .authenticationProvider(getProvider())
                    .authorizeRequests().antMatchers("/api/auth/login").permitAll()
                    .antMatchers(HttpMethod.POST,"/api/auth/validate").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/auth/validate").authenticated()
                    .antMatchers("/api/auth/register").permitAll()
                    .anyRequest().authenticated();
                

        // http
        //     .authenticationProvider(getProvider())
        //     .formLogin()
        //         .loginProcessingUrl("/login")
        //         .permitAll()
        //         .and()
        //     .logout()
        //         .logoutUrl("/logout")
        //         .permitAll()
        //         .invalidateHttpSession(true)
        //         .and()
        //     .authorizeRequests()
        //         .antMatchers("/api/**").authenticated()
        //         .anyRequest().authenticated();
            
        // Add a filter to validate the tokens with every request
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}

