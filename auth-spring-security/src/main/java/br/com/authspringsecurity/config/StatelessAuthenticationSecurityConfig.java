package br.com.authspringsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.authspringsecurity.filter.StatelessAuthenticationFilter;
import br.com.authspringsecurity.filter.StatelessLoginFilter;
import br.com.authspringsecurity.service.TokenAuthenticationService;
import br.com.authspringsecurity.service.UserDetailService;

@EnableWebSecurity
@Configuration
@Order(1)
public class StatelessAuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailService userDetailService;
	
	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;
	
	public StatelessAuthenticationSecurityConfig(){
		super(true);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.exceptionHandling().and()
		.anonymous().and()
		.servletApi().and()
		.headers().and()
		.authorizeRequests()
						
		//allow anonymous resource requests
		.antMatchers("/").permitAll()
		.antMatchers("/resources/**").permitAll()
		
		//allow anonymous POSTs to login
		.antMatchers(HttpMethod.POST, "/api/login").permitAll()
		
		//allow anonymous GETs to API
		.antMatchers(HttpMethod.GET, "/api/**").permitAll()
		
		//defined Admin only API area
		.antMatchers("/admin/**").hasRole("ADMIN")
		
		//all other request need to be authenticated
		.anyRequest().hasRole("USER").and()				

		// custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
		.addFilterBefore(new StatelessLoginFilter("/api/login", tokenAuthenticationService, userDetailService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

		// custom Token based authentication based on the header previously given to the client
		.addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
	}
	
}
