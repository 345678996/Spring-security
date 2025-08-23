package com.security.project.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.security.project.exceptions.CustomAccessDeniedHandler;
import com.security.project.exceptions.CustomBasicAuthenticationEntryPoint;
import com.security.project.filter.CsrfCookieFilter;

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collections;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfigurations { 

    @Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
		// http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        // rcc -> request channel configuration
        // smc -> session management config
        http.securityContext(contextConfig -> contextConfig.requireExplicitSave(false))
            .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setMaxAge(3600L);
                    return config;
                }
            }))
            .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                            .ignoringRequestMatchers("/contact", "/register")
                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
            .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()) // Only HTTP
            .authorizeHttpRequests((requests) -> requests
                                        // .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                                        // .requestMatchers( "/myBalance").hasAuthority("VIEWBALANCE")
                                        // .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
                                        // .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                                        .requestMatchers("/myAccount").hasRole("USER")
                                        .requestMatchers( "/myBalance").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers("/myLoans").hasRole("USER")
                                        .requestMatchers("/myCards").hasRole("USER")
                                        .requestMatchers("/user").authenticated()
                                        .requestMatchers("/notices", "/contact", "/error", "/register", "invalidSession").permitAll())
		    .formLogin(withDefaults())
		// hbc -> http basic config
		    .httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()))
        // ehc -> exception handling config
            .exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        // http.exceptionHandling(ehc -> ehc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
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
