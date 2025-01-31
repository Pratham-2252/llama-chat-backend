package com.prathamesh.app.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prathamesh.app.domain.User;
import com.prathamesh.app.dto.UserInfo;
import com.prathamesh.app.exceptions.UserAlreadyExistsException;
import com.prathamesh.app.repository.UserRepository;
import com.prathamesh.app.utility.Email;
import com.prathamesh.app.utility.EmailService;

@Service
public class UserService implements UserDetailsService, IUser {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
			EmailService emailService) {
		super();
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;

		modelMapper.addMappings(new PropertyMap<User, UserInfo>() {
			@Override
			protected void configure() {
				skip(destination.getPassword()); // Exclude password from the mapping
			}
		});
	}

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
	public User save(UserInfo userInfo) {

		if (getByUserName(userInfo.getUserName()).isPresent()) {

			throw new UserAlreadyExistsException("User with email " + userInfo.getUserName() + " already exists");
		}

		User user = modelMapper.map(userInfo, User.class);

		user.setUserId(UUID.randomUUID());
		user.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		user.setActive(true);
		user.setCreateBy("System");
		user.setUpdateBy("System");
		user.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		user.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));

		return userRepository.save(user);
	}

	@Override
	public void update(UUID userId, UserInfo userInfo) {

		userRepository.findByUserId(userId).ifPresent(user -> {

			user.setFirstName(userInfo.getFirstName());

			userRepository.save(user);
		});
	}

	@Override
	public UserInfo getUserByUserId(UUID userId) {

		return userRepository.findByUserId(userId).map(user -> {

			UserInfo userInfo = modelMapper.map(user, UserInfo.class);

			return userInfo;
		}).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Override
	public void sendMail() {

		Email email = new Email();

		email.setTo("patilpg2002@gmail.com");

		email.setSubject("Test mail");

		String body = """
				<html>
				    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
				        <div style="text-align: center; margin-bottom: 20px;">
				            <img src='cid:logoImage' alt='Company Logo' style="width: 150px;"/>
				        </div>
				        <h2 style="color: #007bff;">Hello, User!</h2>
				        <p>Thank you for being part of our platform. Please find the attached PDF for more details.</p>
				        <p>Best regards,<br/><strong>Your Company Name</strong></p>
				    </body>
				</html>
				""";

		email.setBody(body);

		emailService.sendEmail(email);
	}

}
