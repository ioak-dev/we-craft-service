package com.wecraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@PropertySources({
		@PropertySource("classpath:/spring-boot.properties")
})
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class WeCraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeCraftApplication.class, args);
	}

}
