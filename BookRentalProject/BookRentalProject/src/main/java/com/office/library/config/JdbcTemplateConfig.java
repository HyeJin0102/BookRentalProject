package com.office.library.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class JdbcTemplateConfig {
	
	//@Value를 이용해 프로퍼티 초기화. dev.info.properties -> JdbcTemplateConfig.java -> properties-context.xml로 전달됨
	@Value("#{infoProperty['db.driver']}")
	private String dbDriver;
	
	@Value("#{infoProperty['db.url']}")
	private String dbUrl;
	
	@Value("#{infoProperty['db.username']}")
	private String dbUsername;
	
	@Value("#{infoProperty['db.password']}")
	private String dbPassword;
	
	//Bean과 Set메서드를 이용해 DriveManagerDataSource 생성.
	@Bean
	public DataSource dataSource() {
		System.out.println("[JdbcTemplateConfig] dataSource()");
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(dbDriver);
		dataSource.setUrl(dbUrl);
		dataSource.setUsername(dbUsername);
		dataSource.setPassword(dbPassword);
		
		return dataSource;
	}
	
	//JdbcTemplate 빈을 생성하여 반환. 이때 매개변수로 dataSource()를 호출해서 반환받는 dataSource 빈을 전달함
	@Bean
	public JdbcTemplate jdbcTemplate() {
		System.out.println("[JdbcTemplateConfig] jdbcTemplate()");
		
		return  new JdbcTemplate(dataSource());
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager() {
		System.out.println("[JdbcTemplateConfig] transactionManager()");
		
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource());
		
		return transactionManager;
	}
}
