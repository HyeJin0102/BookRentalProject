package com.office.library.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class MultipartResolverConfig {

	@Value("#{commInfoProperty['uploadfile.size']}")
	private long fileSize;
	
	@Value("#{commInfoProperty['uploadfile.encoding']}")
	private String fileEncoding;
	
	//multipartResolver()는 CommonsMultipartResolver를 생성해서 반환하는데, 이때 파일의 최대 사이즈(fileSize)와 기본 인코딩(fileEncoding)을 설정함
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		System.out.println("[MultipartResolverConfig] multipartResolver()");
		
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(fileSize);
		multipartResolver.setDefaultEncoding(fileEncoding);
		
		return multipartResolver;
		
	}
	
}
