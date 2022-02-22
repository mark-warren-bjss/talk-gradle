package org.greeting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.greeting.model.Language;
import org.greeting.repository.GreetingRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GreetingService {

    private final GreetingRepository repository;

    public void sayHello(Language language) {
        log.info("Requested language: {}", language);
        String greetingText = repository.readGreetingText(language)
                .orElseThrow(() -> new IllegalStateException("Unexpected language: " + language));

        System.out.println(greetingText);
    }
}