package t1.tests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import t1.tests.testContainer.TestContainersConfiguration;

@SpringBootTest
class ApplicationTest extends TestContainersConfiguration {

	@Test
	void contextLoads() {
	}

}
