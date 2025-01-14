package com.prathamesh.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prathamesh.app.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByUserName(String username);
}
