package com.globits.resourceserver.hr.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import com.globits.core.resourceserver.RealmRoleConverter;
import com.globits.resourceserver.sample.dao.ColorRepository;
import com.globits.security.service.UserService;

@EnableWebSecurity
@EnableAutoConfiguration
@ComponentScan(
basePackages = { "com.globits.core.rest", "com.globits.security.rest", "com.globits.hr.rest","com.globits.hr.timesheet.rest", "com.globits.hr.exercise.rest"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    ColorRepository repository;
    
    @Autowired
    UserService userService;
    
	public static final String AUTHORITIES_CLAIM_NAME = "roles";
	private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
		jwtConverter.setJwtGrantedAuthoritiesConverter(new RealmRoleConverter(userService));
		return jwtConverter;
	}
    protected JwtAuthenticationConverter authenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName("ROLE");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
	private JwtAuthenticationConverter jwtAuthenticationConverter1(){
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("user-roles");
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}
	

    @Override
    protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().cors().and().authorizeRequests().anyRequest().permitAll();
    }
    

}
