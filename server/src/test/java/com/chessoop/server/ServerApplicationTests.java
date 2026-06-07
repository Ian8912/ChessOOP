package com.chessoop.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Verifies the Spring context starts against a real PostgreSQL instance.
 *
 * <p>A throwaway Postgres container is started by Testcontainers and wired to the
 * application's datasource automatically via {@link ServiceConnection}, so no
 * profile or property overrides are needed. Requires Docker to be running.</p>
 */
@SpringBootTest
@Testcontainers
class ServerApplicationTests {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

	@Test
	void contextLoads() {
	}

}
