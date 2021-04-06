package com.company.courtmanagement;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestPostgreSQLContainer extends PostgreSQLContainer<TestPostgreSQLContainer> {
    private static final String IMAGE_VERSION = "postgres:12";
    private static TestPostgreSQLContainer container;

    private TestPostgreSQLContainer() {
        super(IMAGE_VERSION);
    }

    public static TestPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new TestPostgreSQLContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
