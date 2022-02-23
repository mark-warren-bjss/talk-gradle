package org.greeting.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.greeting.model.Language;
import org.jooq.DSLContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

import static org.greeting.jooq.Tables.GREETING_TEXT;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GreetingRepository {

    private final DSLContext dsl;

    public Optional<String> readGreetingText(Language language) {
        return dsl.selectFrom(GREETING_TEXT)
                .where(GREETING_TEXT.LANGUAGE.eq(language.name()))
                .fetchOptional(GREETING_TEXT.CONTENT);
    }
}
