package com.socialread;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class SendEmail {
	private int otp;
	private void generateOtp() {
		otp = (int)(Math.random()*(999999-100000+1)+100000); 
}
	public int sendmail(String recepient) throws AddressException, MessagingException, IOException {
		   Properties props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", "smtp.gmail.com");
		   props.put("mail.smtp.port", "587");
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication("<email>", "<key>");
		      }
		   });
		   Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress("<email>", false));

		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient));
		   msg.setSubject("Social Reads");
		   msg.setContent("Social Reads email", "text/html");
		   msg.setSentDate(new Date());
		   
		   MimeBodyPart messageBodyPart = new MimeBodyPart();
		   generateOtp();
		   messageBodyPart.setContent("Your one time password is: " + otp, "text/html");

		   Multipart multipart = new MimeMultipart();
		   multipart.addBodyPart(messageBodyPart);
		   msg.setContent(multipart);
		   Transport.send(msg); 
		   return otp;
		}
}
