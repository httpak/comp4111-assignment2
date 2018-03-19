package spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spring.lib.MongoDB;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		MongoDB.init();
		SpringApplication.run(Application.class, args);
	}

}
