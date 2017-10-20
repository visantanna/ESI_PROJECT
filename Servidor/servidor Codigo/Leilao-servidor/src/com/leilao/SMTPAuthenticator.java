package com.leilao;

import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends javax.mail.Authenticator{

	final String senderEmailID = "daniel.schmid.mariotto@gmail.com";
	final String senderPassword = "testeemailservidor";	
	
	public PasswordAuthentication getPasswordAuthentication(){
		 
		return new PasswordAuthentication(senderEmailID, senderPassword);
		 
	}
}
