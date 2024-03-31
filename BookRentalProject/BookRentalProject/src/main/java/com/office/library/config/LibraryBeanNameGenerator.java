package com.office.library.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

public class LibraryBeanNameGenerator implements BeanNameGenerator {

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		System.out.println(definition.getBeanClassName()); //LibraryBeanNameGenerator에 의해 생성된 빈이 서로 다르게 등록되었는지 확인 가능
		return definition.getBeanClassName(); //Bean 아이디 생성
	}

}
