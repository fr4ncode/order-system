package dev.francode.ordersystem;

import dev.francode.ordersystem.service.impl.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class OrderSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderSystemApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}
}
