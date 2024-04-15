package com.poly.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.poly.auth.UserRootService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
	private final UserRootService userRootService;
	private final CustomOAuth2UserService customOAuth2UserService;
	
	
	@Autowired
	CustomOAuth2UserService successHandler;

	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Bean
	  public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userRootService);
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	  }
	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
		        .csrf(AbstractHttpConfigurer::disable)
		        .authorizeHttpRequests(
		            req -> req
		                .requestMatchers("/admin", "/admin/**", "/user/info").authenticated()
		                .anyRequest().permitAll())
		        .formLogin(form -> form
		            .loginPage("/user/login")
		            .usernameParameter("username")
		            .passwordParameter("password")
		            
		            .loginProcessingUrl("/account/login-check")
		            .defaultSuccessUrl("/user/index")
		            .failureUrl("/user/fail"))
		        .oauth2Login(ou -> ou
		        		.successHandler(successHandler)
		            .userInfoEndpoint(e -> e.userService(customOAuth2UserService)))
		        .exceptionHandling(e -> e.accessDeniedPage("/user/denied"))
		        .build();
		  }
	}
	

