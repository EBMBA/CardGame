package com.sp.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sp.controller.AppAuthProvider;
import com.sp.controller.JwtAuthEntryPoint;
import com.sp.controller.JwtRequestFilter;
import com.sp.controller.UserManagementService;

@EnableWebSecurity
public class SecurityConfig {
    // @Autowired
    UserManagementService authService;

    // @Autowired
    private PasswordEncoder passwordEncoder;

    private final JwtAuthEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(UserManagementService authService, 
                            JwtAuthEntryPoint jwtAuthenticationEntryPoint,
                            JwtRequestFilter jwtRequestFilter,
                            PasswordEncoder passwordEncoder){
        
        this.authService = authService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
        this.passwordEncoder = passwordEncoder;
    }



    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception{
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider getProvider(){
        AppAuthProvider provider = new  AppAuthProvider();
        provider.setUserDetailsService(authService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

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
                    .authorizeRequests().antMatchers("/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/users").permitAll()
                    .antMatchers("/api/cards/**").permitAll()
                    .antMatchers("/api/users/**").permitAll()
                    .antMatchers("/api/inventories/**").permitAll()
                    .antMatchers("/api/store/**").permitAll()

                    // .antMatchers(HttpMethod.GET, "/api/users").hasAuthority( "ROLE_ADMIN")
                    // .antMatchers("/api/cards/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                    // .antMatchers("/api/cards").hasAnyAuthority("ROLE_ADMIN")
                    // .antMatchers("/api/users/**").hasAuthority( "ROLE_ADMIN")
                    .antMatchers("/api/wallets/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                    // .antMatchers("/api/inventories/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                    // .antMatchers("/api/store/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
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

