package com.huatu.tiku.interview;

import com.huatu.tiku.interview.entity.ClickButton;
import com.huatu.tiku.interview.entity.ViewButton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@ComponentScan(basePackageClasses = InterviewApplication.class)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class,MultipartAutoConfiguration.class})
@EnableScheduling
public class InterviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterviewApplication.class, args);
	}


}
