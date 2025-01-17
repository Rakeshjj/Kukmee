package com.kukmee.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.kukmee.service.UserDetailServiceImpl;
import com.kukmee.utils.AuthEntryPointJwt;
import com.kukmee.utils.AuthTokenFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

	@Autowired
	private UserDetailServiceImpl userDetailServiceImplementation;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailServiceImplementation);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public SimpleUrlHandlerMapping faviconHandlerMapping() {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(Integer.MIN_VALUE);
		mapping.setUrlMap(Collections.singletonMap("/favicon.ico", faviconRequestHandler()));
		return mapping;
	}

	@Bean
	protected ResourceHttpRequestHandler faviconRequestHandler() {
		ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
		List<Resource> locations = Collections.singletonList(new ClassPathResource("static/favicon.ico"));
		requestHandler.setLocations(locations);
		return requestHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authconfig) throws Exception {
		return authconfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/favicon.ico").permitAll()
						.requestMatchers("/api/auth/**").permitAll().requestMatchers("/api/auth/me").authenticated()
						.requestMatchers("/api/franchise/inquiry").permitAll()
						.requestMatchers("/payment/v1/success", "/payment/v1/cancel").permitAll()
						.requestMatchers("/payment/v1/subscriptionsuccess", "/payment/v1/subscriptioncancel").permitAll()
						.requestMatchers("/payment/v1/chefsuccess", "/payment/v1/chefcancel").permitAll()
						.requestMatchers("/payment/v1/kukmartsuccess", "/payment/v1/kukmartcancel").permitAll()
						.requestMatchers("/payment/v1/cateringsuccess", "/payment/v1/cateringcancel").permitAll()
						.requestMatchers("/payment/v1/cooksuccess", "/payment/v1/cookcancel").permitAll()
						.requestMatchers("/api/auth/reset-password").permitAll()
						.requestMatchers("/payment/v1/monthlycooksuccess", "/payment/v1/monthlycookcancel").permitAll()
						.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml")
						.permitAll().requestMatchers("/api/fooditems/save").hasRole("ADMIN")
						.requestMatchers("/api/bartender/book").authenticated().requestMatchers("/favicon.ico")
						.permitAll().anyRequest().authenticated());

		http.authenticationProvider(authenticationProvider());
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);
		configuration.addExposedHeader("Authorization");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
