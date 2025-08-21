package com.security.project.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.security.project.exceptions.CustomAccessDeniedHandler;
import com.security.project.exceptions.CustomBasicAuthenticationEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfigurations { 

    @Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		// http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        // rcc -> request channel configuration
        // smc -> session management config
        http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidSession").maximumSessions(3).maxSessionsPreventsLogin(true))
            .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()) // Only HTTP
            .authorizeHttpRequests((requests) -> requests
                                        .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                                        .requestMatchers("/notices", "/contact", "/error", "/register", "invalidSession", "/user").permitAll())
		    .formLogin(withDefaults())
		// hbc -> http basic config
		    .httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()))
        // ehc -> exception handling config
            .exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()))
        // http.exceptionHandling(ehc -> ehc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
            .csrf(csrfConfig -> csrfConfig.disable());
		return http.build();
	}

    // DataSource object is populated by application.property file.
    // @Bean
    // public UserDetailsService userDetailsService(DataSource dataSource) {
    //     UserDetails user = User.withUsername("Kanav").password("{noop}1234.").authorities("read").build();
    //     UserDetails admin = User.withUsername("admin").password("{bcrypt}$2a$12$UJKcDI5UtTc4DW7NIsPGW.SsAeSzq.PMvaxzwKgegOL.aRj0T.Eve").authorities("admin").build();
    //     return new JdbcUserDetailsManager(dataSource);
    // }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Check wether the user given password is used in any of the dat breach? i.e recommend user to use strong password
    // @Bean
    // public CompromisedPasswordChecker compromisedPasswordChecker() {
    //     return new HaveIBeenPwnedRestApiPasswordChecker();
    // }

}
