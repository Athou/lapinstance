package be.hehehe.lapinstance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LapinstanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LapinstanceApplication.class, args);
	}

}
