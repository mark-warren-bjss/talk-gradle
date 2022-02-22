package org.greeting.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.greeting.model.Language;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GreetingRepository {

    public Optional<String> readGreetingText(Language language) {
        switch (language) {
            case EN:
                return Optional.of("Hello, pleased to meet you.");
            case ES:
                return Optional.of("Hola, encantado a conoceros.");
            default:
                return Optional.empty();
        }
    }
}
