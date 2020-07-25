package com.maqs.reportgenerator.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private static final long MAX_UPLOAD_SIZE = 20;

	@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        DataSize size = DataSize.ofMegabytes(MAX_UPLOAD_SIZE);
        factory.setMaxFileSize(size);
        factory.setMaxRequestSize(size);
        return factory.createMultipartConfig();
    }
	
	@Bean
    public RestTemplate restTemplate() {
		return new RestTemplate();
    }
}