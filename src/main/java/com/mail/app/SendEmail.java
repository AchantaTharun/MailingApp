package com.mail.app;

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

    Properties emailProperties;

    Session mailSession;

    MimeMessage emailMessage;

    public void setMailServerProperties() {

	String emailPort = "587";// gmail's smtp port

	emailProperties = System.getProperties();
	emailProperties.put("mail.smtp.port", emailPort);
	emailProperties.put("mail.smtp.auth", "true");
	emailProperties.put("mail.smtp.starttls.enable", "true");

    }

    public void createEmailMessage(String rec, String sbj, String msg) throws AddressException, MessagingException {

	String[] toEmails = { rec };
	String emailSubject = sbj;
	String emailBody = msg;

	mailSession = Session.getDefaultInstance(emailProperties, null);
	emailMessage = new MimeMessage(mailSession);

	for (int i = 0; i < toEmails.length; i++) {
	    emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
	}

	emailMessage.setSubject(emailSubject);
	emailMessage.setContent(emailBody, "text/html");
	// emailMessage.setText(emailBody);// for a text email

    }

    // throws AddressException, MessagingException
    public int sendEmail(String from, String pword) {

	try {
	    String emailHost = "smtp.googlemail.com";
	    String fromUser = from;// just the id alone without @gmail.com
	    String fromUserEmailPassword = pword;

	    Transport transport = mailSession.getTransport("smtp");

	    transport.connect(emailHost, fromUser, fromUserEmailPassword);
	    transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
	    transport.close();
	    System.out.println("Email sent successfully.");
	    return 1;

	} catch (SendFailedException ae) {
	    System.out.println("Invalid Email Address");
	    return 2;
	} catch (NullPointerException ae) {
	    System.out.println("no email address");
	    return 22;
	} catch (AuthenticationFailedException afe) {
	    System.out.println("Authentication failed");
	    return 3;
	} catch (Exception e) {

	    System.out.println(e);
	    return 0;
	}
    }
}