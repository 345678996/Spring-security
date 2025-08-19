package com.security.project.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfigurations { 

    @Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		// http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.authorizeHttpRequests((requests) -> requests
                                        .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                                        .requestMatchers("/notices", "/contact", "/error").permitAll());
		http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
		return http.build();
	}

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("Kanav").password("{noop}1234").authorities("read").build();
        UserDetails admin = User.withUsername("admin").password("{noop}54321").authorities("admin").build();
        return new InMemoryUserDetailsManager(user, admin);
    }

}
