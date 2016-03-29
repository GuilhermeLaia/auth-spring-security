package br.com.authspringsecurity.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.authspringsecurity.domain.UserAuthentication;
import br.com.authspringsecurity.entity.User;
import br.com.authspringsecurity.service.TokenAuthenticationService;
import br.com.authspringsecurity.service.UserDetailService;

public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

	private final TokenAuthenticationService tokenAuthenticationService;
	private final UserDetailService  userDetailService;
	
	
	public StatelessLoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService,
			UserDetailService userDetailService, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(urlMapping));
		this.userDetailService = userDetailService;
		this.tokenAuthenticationService = tokenAuthenticationService;
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException,
			IOException, ServletException {
		
		final User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword());
		return getAuthenticationManager().authenticate(loginToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authentication) throws IOException, ServletException {

		// Lookup the complete User object from the database and create an Authentication for it
		final User authenticatedUser = userDetailService.loadUserByUsername(authentication.getName());
		final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

		// Add the custom token as HTTP header to the response
		tokenAuthenticationService.addAuthentication(response, userAuthentication);

		// Add the authentication to the Security context
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
	}
}
