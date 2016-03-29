package br.com.authspringsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.authspringsecurity.entity.User;
import br.com.authspringsecurity.repository.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
	
	@Override
	public final User loadUserByUsername(String userName) throws UsernameNotFoundException {
		final User user = userRepository.findByUserName(userName);
		if(user == null){
			throw new UsernameNotFoundException("user name not found");
		}
		detailsChecker.check(user);
		return user;
	}

}
