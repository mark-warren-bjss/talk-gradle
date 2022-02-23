package org.greeting.config;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

public class DSLContextConfig {
    @Produces
    DSLContext dslContext(DataSource dataSource) {
        return DSL.using(new DefaultConfiguration()
                .set(SQLDialect.POSTGRES)
                .set(dataSource));
    }
}