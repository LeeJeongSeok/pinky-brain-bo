package com.jeongseok.pinkybrainbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PinkyBrainBoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PinkyBrainBoApplication.class, args);
	}

}
