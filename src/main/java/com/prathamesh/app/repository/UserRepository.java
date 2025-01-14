package com.prathamesh.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prathamesh.app.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByUserName(String username);

	public Optional<User> findByUserId(UUID userId);
}
