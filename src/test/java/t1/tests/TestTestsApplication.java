package t1.tests;

import org.springframework.boot.SpringApplication;

public class TestTestsApplication {

	public static void main(String[] args) {
		SpringApplication.from(TestsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
