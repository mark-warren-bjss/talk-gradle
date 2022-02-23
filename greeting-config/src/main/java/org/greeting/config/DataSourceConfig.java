package org.greeting.config;

import javax.enterprise.inject.Produces;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class DataSourceConfig {
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://127.0.0.1:5437/APP";
    private static final String USERNAME = "dev";
    private static final String PASSWORD = "dev123";

    @Produces
    DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setValidationQuery("SELECT 1;");
        return dataSource;
    }
}