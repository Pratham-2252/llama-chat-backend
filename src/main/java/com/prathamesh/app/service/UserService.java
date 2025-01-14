package com.prathamesh.app.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prathamesh.app.domain.User;
import com.prathamesh.app.dto.UserInfo;
import com.prathamesh.app.repository.UserRepository;

@Service
public class UserService implements UserDetailsService, IUser {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return userRepository.findByUserName(username).map(user -> {

			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
					user.getAuthorities());

		}).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}

	public Optional<User> getByUserName(String userName) {

		return userRepository.findByUserName(userName);
	}

	@Override
	public void save(UserInfo userInfo) {

		User user = modelMapper.map(userInfo, User.class);

		user.setUserId(UUID.randomUUID());
		user.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		user.setActive(true);
		user.setCreateBy("System");
		user.setUpdateBy("System");
		user.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		user.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));

		userRepository.save(user);
	}

}
