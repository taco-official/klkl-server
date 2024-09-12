package taco.klkl.global.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import taco.klkl.domain.user.domain.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.headers(header ->
				header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
			)
			.authorizeHttpRequests(authorizeRequests ->
				authorizeRequests
					.requestMatchers(PathRequest.toH2Console()).permitAll()
					.requestMatchers("/swagger-ui/**").permitAll()
					.requestMatchers("/", "/login/**").permitAll()
					.requestMatchers(HttpMethod.POST).authenticated()
					.requestMatchers(HttpMethod.PUT).authenticated()
					.requestMatchers(HttpMethod.DELETE).authenticated()
					.requestMatchers(
						"/v1/users/me/**",
						"/v1/products/following/**",
						"/v1/notifications/**"
					).hasRole(Role.USER.name())
					.requestMatchers(
						RegexRequestMatcher.regexMatcher("/v1/products/\\d+/likes(/.*)?"))
					.hasRole(Role.USER.name())
					.requestMatchers(
						"/v1/oauth/**",
						"/v1/users/**",
						"/v1/products/**",
						"/v1/regions/**",
						"/v1/countries/**",
						"/v1/cities/**",
						"/v1/currencies/**",
						"/v1/categories/**",
						"/v1/subcategories/**",
						"/v1/search/**"
					).permitAll()
					.anyRequest().authenticated()
			);

		return http.build();
	}
}
