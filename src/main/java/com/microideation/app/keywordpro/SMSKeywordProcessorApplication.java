package com.microideation.app.keywordpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.microideation.app.keywordpro.config", "com.microideation.app.keywordpro.util"})
public class SMSKeywordProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SMSKeywordProcessorApplication.class, args);
	}
}
