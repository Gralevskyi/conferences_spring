package com.hralievskyi.conferences.repository;

import org.springframework.data.repository.CrudRepository;

import com.hralievskyi.conferences.entity.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
}
