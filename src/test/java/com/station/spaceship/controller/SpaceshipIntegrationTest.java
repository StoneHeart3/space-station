package com.station.spaceship.controller;

import com.station.spaceship.model.Spaceship;
import com.station.spaceship.repository.SpaceshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class SpaceshipIntegrationTest {

    // 1. Spin up a real PostgreSQL container
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);
    // 2. Dynamically inject the container's URL/credentials into Spring Boot
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SpaceshipRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testRegisterShipHitsRealDatabase() {
        // Arrange
        Spaceship ship = new Spaceship();
        ship.setModel("Heavy Freighter");
        ship.setCaptainName("guru_prasath");
        ship.setFuelLevel(100);

        // Act: Hit the actual REST endpoint
        ResponseEntity<Spaceship> response = restTemplate.postForEntity(
                "/api/v1/ships/register", 
                ship, 
                Spaceship.class
        );

        // Assert: Check the HTTP response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isNotNull();
        
        // Assert: Verify the data actually persisted in the PostgreSQL container
        assertThat(repository.findAll()).hasSize(1);
        assertThat(repository.findAll().get(0).getCaptainName()).isEqualTo("guru_prasath");
    }
}
