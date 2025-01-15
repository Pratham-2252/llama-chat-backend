package com.prathamesh.app.utility;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Async
	public void sendEmail(Email email) {

		logger.info("Sending mail to : {}", email.getTo());

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setTo(email.getTo());
			helper.setSubject(email.getSubject());
			helper.setText(email.getBody(), true);

			ClassPathResource classPathResource = new ClassPathResource("static/logo.jpg");

			helper.addInline("logoImage", classPathResource, "image/png");

			FileSystemResource fileSystemResource = new FileSystemResource(
					new File("C:\\Users\\HP\\Downloads\\Kalnirnay-Calendar-2025.pdf"));

			helper.addAttachment("Document.pdf", fileSystemResource);

			javaMailSender.send(mimeMessage);

			logger.info("Mail sent to : {}", email.getTo());

		} catch (MessagingException e) {

			logger.error("Error while sending mail to : {}", email.getTo());

			e.printStackTrace();
		}
	}
}
