package org.bitvault.appstore.cloud.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailService {
	@Autowired
	JavaMailSender mailSender;

	public void sendMail(String to, String sub, String body) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage);
		mimeHelper.setFrom("no-reply@appstore.com");
		mimeHelper.setTo(to);
		mimeHelper.setSubject(sub);
		mimeHelper.setText(body, true);
		mailSender.send(mimeMessage);
	}
}
