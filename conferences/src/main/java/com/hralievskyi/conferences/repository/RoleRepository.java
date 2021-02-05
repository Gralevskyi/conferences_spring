package com.hralievskyi.conferences.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hralievskyi.conferences.entity.user.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
