package com.hralievskyi.conferences;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hralievskyi.conferences.entity.Report;
import com.hralievskyi.conferences.entity.user.Role;
import com.hralievskyi.conferences.entity.user.Roles;
import com.hralievskyi.conferences.entity.user.User;
import com.hralievskyi.conferences.repository.RoleRepository;
import com.hralievskyi.conferences.repository.UserRepository;
import com.hralievskyi.conferences.service.ModeratorService;

//@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModeratorService moderatorService;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup)
			return;
		createRoleIfNotFound(Roles.ROLE_USER.toString());
		createRoleIfNotFound(Roles.ROLE_SPEAKER.toString());
		createRoleIfNotFound(Roles.ROLE_MODERATOR.toString());

		Role adminRole = roleRepository.findByName(Roles.ROLE_MODERATOR.toString());
		Role speakerRole = roleRepository.findByName(Roles.ROLE_SPEAKER.toString());
		Role custRole = roleRepository.findByName(Roles.ROLE_USER.toString());
		User user = new User();
		user.setUsername("admin@mail.com");
		user.setPassword(passwordEncoder.encode("names"));
		Set<Role> roles = new HashSet<>();
		roles.add(adminRole);
		roles.add(speakerRole);
		roles.add(custRole);
		user.setRoles(roles);
		userRepository.save(user);

		Set<Role> spRoles = new HashSet<>(roles);
		spRoles.remove(adminRole);
		User speaker = new User();
		speaker.setUsername("dmytro@mail.com");
		speaker.setPassword(passwordEncoder.encode("names"));
		speaker.setRoles(spRoles);
		User createdUser = userRepository.save(speaker);
		moderatorService.createSpeaker(createdUser);

		User speakerB = new User();
		speakerB.setUsername("yuriy@mail.com");
		speakerB.setPassword(passwordEncoder.encode("names"));
		speakerB.setRoles(spRoles);
		User createdUserB = userRepository.save(speakerB);
		moderatorService.createSpeaker(createdUserB);

		User speakerC = new User();
		speakerC.setUsername("maxim@mail.com");
		speakerC.setPassword(passwordEncoder.encode("names"));
		speakerC.setRoles(spRoles);
		User createdUserC = userRepository.save(speakerC);
		moderatorService.createSpeaker(createdUserC);

		Report report = new Report("Object Oriented Programming");
		moderatorService.createReport(report);

		Report report1 = new Report("Java fundamentals");
		moderatorService.createReport(report1);

		Report report2 = new Report("Sprint JPA");
		moderatorService.createReport(report2);

		Report report3 = new Report("Spring Security");
		moderatorService.createReport(report3);

		Report report4 = new Report("XML fundamentals");
		moderatorService.createReport(report4);

		Report report5 = new Report("HTML and CSS");
		moderatorService.createReport(report5);

		alreadySetup = true;
	}

	@Transactional
	Role createRoleIfNotFound(String name) {
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			roleRepository.save(role);
		}
		return role;
	}
}
