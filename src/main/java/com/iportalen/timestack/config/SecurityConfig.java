package com.iportalen.timestack.config;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.iportalen.timestack.config.auth.firebase.FirebaseAuthenticationProvider;
import com.iportalen.timestack.config.auth.firebase.FirebaseFilter;
import com.iportalen.timestack.service.firebase.FirebaseService;
import com.iportalen.timestack.service.user.UserServiceImpl;

@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	public static class Roles {
		public static final String ANONYMOUS = "ANONYMOUS";
		public static final String USER = "USER";
		static public final String ADMIN = "ADMIN";

		private static final String ROLE_ = "ROLE_";
		public static final String ROLE_ANONYMOUS = ROLE_ + ANONYMOUS;
		public static final String ROLE_USER = ROLE_ + USER;
		static public final String ROLE_ADMIN = ROLE_ + ADMIN;
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Configuration
	protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		@Qualifier(value = UserServiceImpl.NAME)
		private UserDetailsService userService;

		@Value("${firebase.enabled}")
		private Boolean firebaseEnabled;

		@Autowired
		private FirebaseAuthenticationProvider firebaseProvider;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userService);
			if (firebaseEnabled) {
				auth.authenticationProvider(firebaseProvider);
			}
		}
	}

	@Configuration
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {
		
		@Value("${firebase.enabled}")
		private Boolean firebaseEnabled;
		
		@Value("${cors.allowedorigins}")
		private String allowedOrigins;

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources",
					"/configuration/security", "/swagger-ui.html", "/webjars/**", "/v2/swagger.json");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			if (firebaseEnabled) {
				http.cors()
					.and()
					.addFilterBefore(tokenAuthorizationFilter(), BasicAuthenticationFilter.class).authorizeRequests()//
					.antMatchers("/api/firebase/**").permitAll()
					.antMatchers("/api/open/**").hasAnyRole(Roles.ANONYMOUS)//
					.antMatchers("/api/client/**").hasRole(Roles.USER)//
					.antMatchers("/api/admin/**").hasAnyRole(Roles.ADMIN)//
					.antMatchers("/health/**").hasAnyRole(Roles.ADMIN)//
					.antMatchers("/**").denyAll()//
					.and().csrf().disable()//
					.anonymous().authorities(Roles.ROLE_ANONYMOUS);//
			} else {
				http.cors()
					.and()
					.httpBasic().and().authorizeRequests()//
					.antMatchers("/api/firebase/**").permitAll()
					.antMatchers("/api/open/**").hasAnyRole(Roles.ANONYMOUS)//
					.antMatchers("/api/client/**").hasRole(Roles.USER)//
					.antMatchers("/api/admin/**").hasAnyRole(Roles.ADMIN)//
					.antMatchers("/health/**").hasAnyRole(Roles.ADMIN)//
					.antMatchers("/**").denyAll()//
					.and().csrf().disable()//
					.anonymous().authorities(Roles.ROLE_ANONYMOUS);//
			}
		}

		@Autowired(required = false)
		private FirebaseService firebaseService;

		private FirebaseFilter tokenAuthorizationFilter() {
			return new FirebaseFilter(firebaseService);
		}
		
		@Bean(name="corsConfigurationSource")
	    CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList(StringUtils.split(allowedOrigins, ",")));
	        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT"));
	        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "X-Authorization-Firebase", "Authorization", "content-type", "x-requested-with", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "x-auth-token", "x-app-id", "Origin","Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
	        configuration.addExposedHeader("X-Firebase-CustomToken");
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }

	}
}