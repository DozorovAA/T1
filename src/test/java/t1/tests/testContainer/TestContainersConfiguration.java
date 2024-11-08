package t1.tests.testContainer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class TestContainersConfiguration {
	private static final PostgreSQLContainer<?> postgres;

	static {
		postgres =  new PostgreSQLContainer<>("postgres:11.13")
				.withDatabaseName("testDb")
				.withPassword("0000")
				.withUsername("postgres");
		postgres.start();
	}

	@DynamicPropertySource
	public static void properties(DynamicPropertyRegistry registry) {
		registry.add("app.db.url", postgres::getJdbcUrl);
		registry.add("app.db.username", postgres::getUsername);
		registry.add("app.db.password", postgres::getPassword);

	}

}
