package com.yoda_of_soda.springfolio.config;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration; 
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.authentication.AuthenticationProvider; 
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; 
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; 
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity; 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; 
import org.springframework.security.config.http.SessionCreationPolicy; 
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.security.web.SecurityFilterChain; 
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.yoda_of_soda.springfolio.repository.UserRepository;
import com.yoda_of_soda.springfolio.services.UserService; 

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig { 

	// @Autowired
	// private JwtAuthFilter authFilter; 
    private UserDetailsService userDetailsService;
	// User Creation 
    
    public SecurityConfig(UserRepository userRepository){
        this.userDetailsService = new UserService(userRepository);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
		return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests.requestMatchers("/hello", "/", "/login", "/users", "/jwt/decode").permitAll())
                .authorizeHttpRequests(requests -> requests.requestMatchers("/auth/user/**").authenticated())
                .authorizeHttpRequests(requests -> requests.requestMatchers("/auth/admin/**").authenticated())
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                // .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build(); 
	} 

	// Password Encoding 
	@Bean
	public PasswordEncoder passwordEncoder() { 
		return new BCryptPasswordEncoder(); 
	} 

	@Bean
	public AuthenticationProvider authenticationProvider() { 
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
		authenticationProvider.setUserDetailsService(getUserDetailsService()); 
		authenticationProvider.setPasswordEncoder(passwordEncoder()); 
		return authenticationProvider; 
	} 

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
		return config.getAuthenticationManager(); 
	} 

} 

