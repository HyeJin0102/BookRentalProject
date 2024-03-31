package com.office.library.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailSenderConfig {

	@Value("#{commInfoProperty['mail.host']}")
	private String mailHost;
	
	@Value("#{commInfoProperty['mail.port']}")
	private int mailPort;
	
	@Value("#{commInfoProperty['mail.username']}")
	private String mailUserName;
	
	@Value("#{commInfoProperty['mail.password']}")
	private String mailPassword;
	
	@Value("#{commInfoProperty['mail.smtp.auth']}")
	private String mailSmtpAuth;
	
	@Value("#{commInfoProperty['mail.smtp.starttls.enable']}")
	private String mailSmtpStarttlsEnable;
	
	//mailSender()는 JavaMailSenderImpl를 생성해 반환함. 그리고 JavaMailProperties 멤버 필드는 java.util 패키지의 Properties를 이용해 초기화함
	@Bean
	public JavaMailSenderImpl mailSender() {
		System.out.println("[MailSenderConfig] mailSender()");
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailHost);
		mailSender.setPort(mailPort);
		mailSender.setUsername(mailUserName);
		mailSender.setPassword(mailPassword);
		
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", mailSmtpAuth);
		properties.setProperty("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
		
		mailSender.setJavaMailProperties(properties);
		
		return mailSender;
		
	}
	
}
