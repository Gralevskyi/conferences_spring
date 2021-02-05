package com.hralievskyi.conferences.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hralievskyi.conferences.entity.user.Role;
import com.hralievskyi.conferences.entity.user.User;
import com.hralievskyi.conferences.repository.UserRepository;

@Service
@Transactional // org.springframework.security.authentication.InternalAuthenticationServiceException:
				// failed to lazily initialize a collection of role
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserRepository userRepo;

	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (user != null) {
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapToGrantedAuthorities(user.getRoles()));
		}
		throw new UsernameNotFoundException("User '" + username + "' not found");
	}

	private List<GrantedAuthority> mapToGrantedAuthorities(Set<Role> set) {
		return set.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	public String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			return currentUserName;
		}
		return null;
	}

}
