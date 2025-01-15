package com.prathamesh.app.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

			// Attachment code.
//			helper.addAttachment("imageName", imageSource);			
//			helper.addAttachment("pdfName", pdfSource);

			javaMailSender.send(mimeMessage);

			logger.info("Mail sent to : {}", email.getTo());

		} catch (MessagingException e) {

			logger.error("Error while sending mail to : {}", email.getTo());

			e.printStackTrace();
		}
	}
}
