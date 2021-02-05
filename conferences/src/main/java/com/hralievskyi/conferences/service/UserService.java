package com.hralievskyi.conferences.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hralievskyi.conferences.entity.user.Role;
import com.hralievskyi.conferences.entity.user.User;
import com.hralievskyi.conferences.repository.RoleRepository;
import com.hralievskyi.conferences.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Set<Role> roles = new HashSet<>();
		roles.add(roleRepo.findByName("ROLE_CUSTOMER"));
		user.setRoles(roles);
		userRepo.save(user);
	}

	public User findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public boolean isUsernameAlreadyExist(String username) {
		User user = userRepo.findByUsername(username);
		if (user != null) {
			return true;
		}
		return false;
	}

	public Role findRoleByName(String rolename) {
		return roleRepo.findByName(rolename);
	}

}
